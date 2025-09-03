package co.com.crediya.pragma.api.helper;

import co.com.crediya.pragma.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SecurityContextHelper {

    public Mono<User> getCurrentUser(ServerWebExchange exchange) {
        SecurityContext context = exchange.getAttribute(SecurityContext.class.getName());
        
        if (context == null || context.getAuthentication() == null) {
            log.warn("No security context found in exchange");
            return Mono.error(new RuntimeException("Usuario no autenticado"));
        }

        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            log.debug("User found in security context: {}", ((User) principal).getEmail());
            return Mono.just((User) principal);
        }

        log.warn("Principal is not a User instance: {}", principal.getClass().getSimpleName());
        return Mono.error(new RuntimeException("Usuario no autenticado"));
    }

    public Mono<User> getCurrentUserFromRequest(ServerWebExchange exchange) {
        return getCurrentUser(exchange)
                .doOnNext(user -> log.debug("Current user: {}", user.getEmail()))
                .doOnError(error -> log.error("Error getting current user: {}", error.getMessage()));
    }
}
