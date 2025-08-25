package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.dto.SaveUserDTO;
import co.com.crediya.pragma.api.helper.DtoValidator;
import co.com.crediya.pragma.api.mapper.UserMapper;
import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final DtoValidator dtoValidator;
    private final UserMapper userMapper;

    public Mono<ServerResponse> listenSaveUser(ServerRequest req) {
        return req.bodyToMono(SaveUserDTO.class)
                .flatMap(dtoValidator::validate)
                .map(userMapper::toDomain)
                .flatMap(userUseCase::saveUser)
                .flatMap(saved -> ServerResponse
                        .created(URI.create("/api/v1/users/id/" + saved.getUserId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(saved)
                );
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return userUseCase.getUserByIdNumber(id)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    public Mono<ServerResponse> listenDeleteUser(ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return userUseCase.deleteUser(id)
                .then(ServerResponse.noContent().build());
    }

}
