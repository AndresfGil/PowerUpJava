package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.dto.SaveUserDTO;
import co.com.crediya.pragma.api.helper.DtoValidator;
import co.com.crediya.pragma.api.mapper.HttpReactiveLogger;
import co.com.crediya.pragma.api.mapper.UserMapper;
import co.com.crediya.pragma.api.service.UserTransactionalService;
import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final DtoValidator dtoValidator;
    private final UserMapper userMapper;
    private final UserTransactionalService userTransactionalService;



    public Mono<ServerResponse> listenSaveUser(ServerRequest req) {
        Mono<ServerResponse> flow = req.bodyToMono(SaveUserDTO.class)
                .flatMap(dtoValidator::validate)
                .map(userMapper::toDomain)
                .flatMap(userTransactionalService::saveUser)
                .flatMap(saved -> ServerResponse
                        .created(URI.create("/api/v1/users/id/" + saved.getUserId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(saved));
        return HttpReactiveLogger.logMono(req, flow, "create user");
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest req) {
        Mono<ServerResponse> flow = ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.getAllUsers(), User.class);
        return HttpReactiveLogger.logMono(req, flow, "list users");
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest req) {
        Long id = Long.valueOf(req.pathVariable("id"));
        Mono<ServerResponse> flow = userUseCase.getUserByIdNumber(id)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
        return HttpReactiveLogger.logMono(req, flow, "get user by id");
    }

    public Mono<ServerResponse> listenDeleteUser(ServerRequest req) {
        Long id = Long.valueOf(req.pathVariable("id"));
        Mono<ServerResponse> flow = userUseCase.deleteUser(id)
                .then(ServerResponse.noContent().build());
        return HttpReactiveLogger.logMono(req, flow, "delete user");
    }

}
