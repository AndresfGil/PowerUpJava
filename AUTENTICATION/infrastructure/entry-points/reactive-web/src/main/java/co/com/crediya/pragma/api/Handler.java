package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.dto.LoginDTO;
import co.com.crediya.pragma.api.dto.SaveUserDTO;
import co.com.crediya.pragma.api.dto.UserResponseDTO;
import co.com.crediya.pragma.api.dto.BatchUsersRequestDTO;
import co.com.crediya.pragma.api.helper.DtoValidator;
import co.com.crediya.pragma.api.mapper.HttpReactiveLogger;
import co.com.crediya.pragma.api.mapper.LoginMapper;
import co.com.crediya.pragma.api.mapper.UserMapper;
import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.usecase.login.LoginUseCase;
import co.com.crediya.pragma.usecase.user.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final LoginUseCase loginUseCase;
    private final LoginMapper  loginMapper;
    private final DtoValidator dtoValidator;
    private final UserMapper userMapper;

    public Mono<ServerResponse> listenSaveUser(ServerRequest req) {
        log.info("Iniciando saveUser - Path: {}", req.path());

        Mono<ServerResponse> flow = extractTokenFromRequest(req)
                .doOnNext(token -> log.info("Token extraído: {}", token.substring(0, Math.min(token.length(), 20)) + "..."))
                .flatMap(loginUseCase::getUserFromToken)
                .doOnNext(user -> log.info("Usuario autenticado: {}", user.getEmail()))
                .flatMap(currentUser -> req.bodyToMono(SaveUserDTO.class)
                        .doOnNext(dto -> log.info("DTO recibido para usuario: {}", dto.getEmail()))
                        .flatMap(dtoValidator::validate)
                        .doOnNext(validatedDto -> log.debug("DTO validado exitosamente: {}", validatedDto.getEmail()))
                        .doOnError(error -> log.error("Error de validación en SaveUserDTO: {}", error.getMessage()))
                        .map(userMapper::toDomain)
                        .flatMap(user -> userUseCase.saveUserWithAuthorization(user, currentUser.getRolId()))
                        .doOnNext(saved -> log.info("Usuario guardado exitosamente: {}", saved.getEmail()))
                        .map(saved -> new UserResponseDTO(
                                saved.getName(),
                                saved.getLastname(),
                                saved.getEmail(),
                                saved.getBaseSalary(),
                                saved.getPhone(),
                                saved.getAddress()
                        ))
                        .flatMap(responseDto -> ServerResponse
                                .created(URI.create("/api/v1/users/id/" + responseDto.getEmail()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(responseDto)));

        return HttpReactiveLogger.logMono(req, flow, "create user");
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest req) {
        log.info("Iniciando getAllUsers - Path: {}", req.path());

        Mono<ServerResponse> flow = extractTokenFromRequest(req)
                .doOnNext(token -> log.info("Token extraído: {}", token.substring(0, Math.min(token.length(), 20)) + "..."))
                .flatMap(loginUseCase::getUserFromToken)
                .doOnNext(user -> log.info("Usuario autenticado: {}", user.getEmail()))
                .then(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userUseCase.getAllUsers(), User.class));

        return HttpReactiveLogger.logMono(req, flow, "list users");
    }

    public Mono<ServerResponse> listenLogin(ServerRequest req) {
        log.info("Iniciando login - Path: {}", req.path());

        Mono<ServerResponse> flow = req.bodyToMono(LoginDTO.class)
                .flatMap(dtoValidator::validate)
                .doOnNext(loginDto -> log.info("Iniciando login para email: {}", loginDto.email()))
                .doOnError(error -> log.error("Error de validación en LoginDTO: {}", error.getMessage()))
                .flatMap(loginDto -> loginUseCase.login(loginDto.email(), loginDto.password()))
                .doOnNext(token -> log.debug("Token generado exitosamente"))
                .doOnError(error -> log.error("Error en proceso de login: {}", error.getMessage()))
                .flatMap(token -> loginUseCase.getUserFromToken(token)
                        .map(user -> loginMapper.toResponse(user, token)))
                .doOnSuccess(responseDto -> log.info("Login exitoso para email: {}", responseDto.email()))
                .flatMap(responseDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseDto));

        return HttpReactiveLogger.logMono(req, flow, "login user");
    }

    public Mono<ServerResponse> listenValidateToken(ServerRequest req) {
        log.info("Iniciando validación de token - Path: {}", req.path());

        Mono<ServerResponse> flow = extractTokenFromRequest(req)
                .flatMap(loginUseCase::getUserFromToken)
                .doOnNext(user -> log.info("Token validado para usuario: {}", user.getEmail()))
                .doOnError(error -> log.error("Error al validar token: {}", error.getMessage()))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of(
                                "valid", true,
                                "user", Map.of(
                                        "email", user.getEmail(),
                                        "name", user.getName(),
                                        "lastname", user.getLastname(),
                                        "rolId", user.getRolId()
                                )
                        )));

        return HttpReactiveLogger.logMono(req, flow, "token validado");
    }

    public Mono<ServerResponse> listenGetUserByToken(ServerRequest req) {
        log.info("Iniciando consulta de usuario autenticado - Path: {}", req.path());

        Mono<ServerResponse> flow = extractTokenFromRequest(req)
                .flatMap(loginUseCase::getUserFromToken)
                .doOnNext(user -> log.info("Usuario autenticado: {}", user.getEmail()))
                .flatMap(authenticatedUser -> {
                    String email = authenticatedUser.getEmail();
                    log.info("Consultando información del usuario autenticado: {}", email);
                    
                    return userUseCase.getUserByEmail(email)
                            .doOnNext(foundUser -> log.info("Usuario encontrado: {}", foundUser.getEmail()))
                            .doOnError(error -> log.error("Error al consultar usuario: {}", error.getMessage()))
                            .flatMap(foundUser -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of(
                                            "email", foundUser.getEmail(),
                                            "name", foundUser.getName(),
                                            "lastname", foundUser.getLastname(),
                                            "rolId", foundUser.getRolId(),
                                            "documentId", foundUser.getDocumentId()
                                    )))
                            .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
                });

        return HttpReactiveLogger.logMono(req, flow, "consultar usuario autenticado");
    }

    public Mono<ServerResponse> listenGetUsersByEmails(ServerRequest req) {
        log.info("Iniciando getUsersByEmails batch - Path: {}", req.path());

        Mono<ServerResponse> flow = extractTokenFromRequest(req)
                .doOnNext(token -> log.info("Token extraído: {}", token.substring(0, Math.min(token.length(), 20)) + "..."))
                .flatMap(loginUseCase::getUserFromToken)
                .doOnNext(user -> log.info("Usuario autenticado: {}", user.getEmail()))
                .then(req.bodyToMono(BatchUsersRequestDTO.class)
                        .doOnNext(batchRequest -> log.info("Procesando batch de {} emails", batchRequest.emails().size()))
                        .flatMap(batchRequest -> userUseCase.getUsersByEmails(batchRequest.emails())
                                .collectList()
                                .doOnNext(users -> log.info("Encontrados {} usuarios del batch", users.size()))
                                .flatMap(users -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(Map.of(
                                                "totalRequested", batchRequest.emails().size(),
                                                "totalFound", users.size(),
                                                "users", users
                                        )))));

        return HttpReactiveLogger.logMono(req, flow, "get users batch");
    }


    private Mono<String> extractTokenFromRequest(ServerRequest req) {
        log.info("Extrayendo token de headers: {}", req.headers().firstHeader("Authorization"));

        return Mono.justOrEmpty(req.headers().firstHeader("Authorization"))
                .map(token -> token.replace("Bearer ", ""))
                .switchIfEmpty(Mono.error(new co.com.crediya.pragma.model.user.exception.InvalidTokenException("Token no proporcionado")));
    }
}
