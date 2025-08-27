package co.com.crediya.pragma.api.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Slf4j
public final class HttpReactiveLogger {

    private HttpReactiveLogger() {}

    public static <T> Mono<T> logMono(ServerRequest req, Mono<T> mono, String action) {
        long t0 = System.currentTimeMillis();
        String method = req.methodName();
        String path = req.path();
        log.info("{} {} - {} - request received", method, path, action);
        return mono
                .doOnSuccess(r ->
                        log.info("{} {} - {} - completed in {} ms",
                                method, path, action, System.currentTimeMillis() - t0))
                .doOnError(e ->
                        log.warn("{} {} - {} - failed in {} ms: {}",
                                method, path, action, System.currentTimeMillis() - t0, e.toString()));
    }
}
