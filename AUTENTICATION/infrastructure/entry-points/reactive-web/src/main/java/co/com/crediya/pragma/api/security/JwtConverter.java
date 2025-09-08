package co.com.crediya.pragma.api.security;

import co.com.crediya.pragma.model.user.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtConverter implements ServerAuthenticationConverter {


    private final JwtService jwt;

    public JwtConverter(JwtService jwt) {
        this.jwt = jwt;
    }


    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        
        // Si es una ruta pública, no procesar autenticación
        if (isPublicPath(path)) {
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
        return path.equals("/api/v1/login") || 
               path.startsWith("/h2/") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/webjars/");
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
