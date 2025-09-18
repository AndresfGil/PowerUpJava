package co.com.crediya.pragma.api.security;

import co.com.crediya.pragma.model.user.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtConverter implements ServerAuthenticationConverter {

    @Value("${app.publicPaths}")
    private List<String> publicPaths;

    private final JwtService jwt;

    public JwtConverter(JwtService jwt) {
        this.jwt = jwt;
    }
    
    @Value("${app.publicPaths}")
    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
        log.info("JwtConverter loaded with public paths: {}", publicPaths);
    }


    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        
        // Si es una ruta pública, no procesar autenticación
        if (isPublicPath(path)) {
            log.debug("Public path accessed: {} - skipping authentication", path);
            return Mono.empty();
        }
        
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return Mono.error(new InvalidTokenException("Token de autorización no proporcionado o formato inválido"));
        }

        String token = header.substring(7);
        if (token.trim().isEmpty()) {
            return Mono.error(new InvalidTokenException("Token vacío"));
        }

        return Mono.fromCallable(() -> jwt.parseAndValidate(token))
                .map(Jws::getPayload)
                .map(this::toAuthentication)
                .onErrorResume(e -> {
                    log.warn("Token validation failed: {}", e.getMessage());
                    return Mono.error(new InvalidTokenException("Token inválido o expirado: " + e.getMessage()));
                });
    }
    
    private boolean isPublicPath(String path) {
        if (publicPaths == null || publicPaths.isEmpty()) {
            // Fallback a rutas hardcodeadas si no se puede cargar la configuración
            return path.equals("/api/v1/login") || 
                   path.equals("/api/v1/health") ||
                   path.equals("/api/v1/ping") ||
                   path.equals("/api/v1/test") ||
                   path.startsWith("/h2/") ||
                   path.startsWith("/v3/api-docs") ||
                   path.startsWith("/swagger-ui") ||
                   path.startsWith("/webjars/") ||
                   path.startsWith("/actuator/");
        }
        
        // Usar la configuración de application.yaml
        return publicPaths.stream().anyMatch(pattern -> {
            if (pattern.endsWith("/**")) {
                String prefix = pattern.substring(0, pattern.length() - 3);
                return path.startsWith(prefix);
            } else if (pattern.endsWith("**")) {
                String prefix = pattern.substring(0, pattern.length() - 2);
                return path.startsWith(prefix);
            } else {
                return path.equals(pattern);
            }
        });
    }


    private Authentication toAuthentication(Claims claims) {
        String userId = claims.getSubject(); // subject = userId en el JwtService


        var authorities = extractRolesFromClaims(claims).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        var authentication = new UsernamePasswordAuthenticationToken(userId, "N/A", authorities);
        authentication.setDetails(claims);
        return authentication;
    }

    private Collection<String> extractRolesFromClaims(Claims claims) {
        Object rawRoles = claims.get("roles");
        if(rawRoles==null) return Collections.emptyList();
        else if(rawRoles instanceof Collection<?>) return ((Collection<?>) rawRoles).stream().map(Objects::toString).collect(Collectors.toSet());
        else {return Set.of(rawRoles.toString());}

    }

}
