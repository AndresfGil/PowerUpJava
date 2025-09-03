package co.com.crediya.pragma.api.config;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.usecase.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final LoginUseCase loginUseCase;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // Ignorar rutas de Swagger, OpenAPI, Actuator y Login
        if (path.startsWith("/swagger-ui") || 
            path.startsWith("/v3/api-docs") || 
            path.startsWith("/actuator") ||
            path.startsWith("/webjars") ||
            path.startsWith("/swagger-ui.html") ||
            path.equals("/api/v1/login")) {
            log.debug("Skipping JWT filter for path: {}", path);
            return chain.filter(exchange);
        }
        
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No JWT token found in request for path: {}, allowing request to continue", path);
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        log.debug("JWT token found for path: {}, token: {}...", path, token.substring(0, Math.min(token.length(), 20)));

        return loginUseCase.getUserFromToken(token)
                .map(user -> {
                    log.debug("Token valid for user: {}, setting security context", user.getEmail());
                    return new UsernamePasswordAuthenticationToken(
                            user, 
                            null, 
                            Collections.emptyList()
                    );
                })
                .map(auth -> {
                    SecurityContext context = new SecurityContextImpl(auth);
                    exchange.getAttributes().put(SecurityContext.class.getName(), context);
                    log.debug("Security context set for user: {}", ((UsernamePasswordAuthenticationToken) auth).getPrincipal());
                    return context;
                })
                .then(chain.filter(exchange))
                .onErrorResume(e -> {
                    log.warn("JWT token validation failed for path {}: {}", path, e.getMessage());
                    // Even if token validation fails, allow the request to continue
                    return chain.filter(exchange);
                });
    }
}
