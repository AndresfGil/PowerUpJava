package co.com.crediya.pragma.api.exception;

import java.util.List;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import org.springframework.lang.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import co.com.crediya.pragma.model.user.exception.BaseException;
import org.springframework.web.bind.support.WebExchangeBindException;
import co.com.crediya.pragma.model.user.exception.ValidationException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;

@Slf4j
@Component
public class GlobalExceptionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @NonNull
    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(BaseException.class, ex -> {
                    log.warn("Handled BaseException code={} status={} path={} msg={}",
                            ex.getErrorCode(), ex.getStatusCode(), request.path(), ex.getMessage());
                    return ServerResponse
                            .status(ex.getStatusCode())
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode(ex.getErrorCode())
                                    .tittle(ex.getTitle())
                                    .message(ex.getMessage())
                                    .errors(ex.getErrors())
                                    .status(HttpStatus.valueOf(ex.getStatusCode()))
                                    .timestamp(ex.getTimestamp())
                                    .build());
                })

                .onErrorResume(WebExchangeBindException.class, ex -> {
                    var errors = ex.getFieldErrors().stream()
                            .map(err -> err.getField() + ": " + err.getDefaultMessage())
                            .toList();

                    var validationEx = new ValidationException(errors);

                    log.warn("Validation exception: {}", errors);

                    return ServerResponse.status(validationEx.getStatusCode())
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode(validationEx.getErrorCode())
                                    .tittle(validationEx.getTitle())
                                    .message(validationEx.getMessage())
                                    .errors(validationEx.getErrors())
                                    .status(HttpStatus.valueOf(validationEx.getStatusCode()))
                                    .timestamp(validationEx.getTimestamp())
                                    .build());
                })

                .onErrorResume(IllegalArgumentException.class, ex -> {
                    log.warn("IllegalArgumentException: {} path={}", ex.getMessage(), request.path());
                    return ServerResponse.status(HttpStatus.BAD_REQUEST)
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode("INVALID_ARGUMENT")
                                    .tittle("Argumento inválido")
                                    .message(ex.getMessage())
                                    .errors(List.of(ex.getMessage()))
                                    .status(HttpStatus.BAD_REQUEST)
                                    .timestamp(LocalDateTime.now())
                                    .build());
                })

                .onErrorResume(Exception.class, ex -> {
                    log.error("Unhandled exception: {} path={}", ex.getMessage(), request.path(), ex);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponse.builder()
                                    .errorCode("INTERNAL_SERVER_ERROR")
                                    .tittle("Error interno del servidor")
                                    .message("Ha ocurrido un error interno. Por favor, inténtelo más tarde.")
                                    .errors(List.of("Error interno del servidor"))
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .timestamp(LocalDateTime.now())
                                    .build());
                });
    }
}
