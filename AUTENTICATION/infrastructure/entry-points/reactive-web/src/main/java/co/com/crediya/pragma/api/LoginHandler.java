package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.dto.LoginDTO;
import co.com.crediya.pragma.api.dto.LoginResponseDTO;

import co.com.crediya.pragma.usecase.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginHandler {

    private final LoginUseCase loginUseCase;



    public Mono<ServerResponse> listenLogin(ServerRequest serverRequest) {
        log.info("Iniciando login - Path: {}", serverRequest.path());

        return serverRequest.bodyToMono(LoginDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body is required")))
                .doOnSubscribe(sub -> log.info("LOGIN Request received"))
                .flatMap(req -> {
                    log.info("LOGIN attempt for email: {}", req.email());
                    return loginUseCase.login(req.email(), req.password());
                })
                .flatMap(t -> {
                    log.info("LOGIN successful for email");
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new LoginResponseDTO(t.accessToken(),"Bearer", t.ttlMinutes()));
                })
                .doOnError(ex -> log.warn("LOGIN failed: {}", ex.getMessage()));

    }

}
