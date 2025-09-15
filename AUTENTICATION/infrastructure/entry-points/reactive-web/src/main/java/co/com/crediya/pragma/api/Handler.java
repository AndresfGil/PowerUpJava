package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.dto.*;
import co.com.crediya.pragma.api.mapper.UserMapper;
import co.com.crediya.pragma.usecase.user.user.UserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final Validator validator;
    private final UserMapper userMapper;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(SaveUserDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body is required")))
                .doOnSubscribe(sub -> log.info("CREATE USER Request received"))
                .flatMap(dto -> {
                    Set<ConstraintViolation<SaveUserDTO>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        log.warn("CREATE USER Validation failed: {} violation(s)", violations.size());
                        return Mono.error(new ConstraintViolationException(violations));
                    }
                    return Mono.just(dto);
                })
                .map(userMapper::toDomain)
                .flatMap(userUseCase::saveUser)
                .doOnSuccess(u -> log.info("CREATE USER User persisted with id={}", u.getUserId()))
                .flatMap(savedUser -> {
                    UserResponseDTO responseDto = new UserResponseDTO(
                            savedUser.getName(),
                            savedUser.getLastname(),
                            savedUser.getEmail(),
                            savedUser.getBaseSalary(),
                            savedUser.getPhone(),
                            savedUser.getAddress()
                    );

                    return ServerResponse.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(responseDto);
                })
                .doOnError(ex -> log.error("CREATE USER Failure creating user: {}", ex.toString()));
    }



    public Mono<ServerResponse> listenValidateUser(ServerRequest serverRequest) {

        return serverRequest
                .bodyToMono(RequestValidateUserDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body is required")))
                .doOnSubscribe(sub -> log.info("[EXISTS USER] Request received"))
                .flatMap(dto -> {
                    Set<ConstraintViolation<RequestValidateUserDTO>> violations =  validator.validate(dto);
                    if (!violations.isEmpty()) {
                        log.warn("EXISTS USER Validation failed: {} violation(s)", violations.size());
                        return Mono.error(new ConstraintViolationException(violations));
                    }

                    return userUseCase.getUserByEmail(dto.email())
                            .flatMap(user -> {
                                log.info("[EXISTS USER] User found by email={}", dto.email());
                                boolean userValid =
                                        user.getDocumentId().equals(dto.documentoIdentidad()) &&
                                                user.getEmail().equals(dto.email());

                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new UserValidateDTO(userValid, userValid ? user.getBaseSalary() : null));
                            })
                            .switchIfEmpty(
                                    ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(new UserValidateDTO(false, null))
                            );
                })
                .doOnError(ex -> log.error("EXISTS USER Failure looking user: {}", ex.toString()));

    }

    public Mono<ServerResponse> listenGetUsersByEmails(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(BatchUsersRequestDTO.class)
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
                                        ))));
    }

}
