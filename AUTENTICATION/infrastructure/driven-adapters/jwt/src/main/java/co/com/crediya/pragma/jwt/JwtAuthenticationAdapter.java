package co.com.crediya.pragma.jwt;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.Role;
import co.com.crediya.pragma.model.user.exception.InvalidTokenException;
import co.com.crediya.pragma.model.user.gateways.AuthenticationGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationAdapter implements AuthenticationGateway {

    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnly}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    public Mono<String> generateToken(User user) {
        return Mono.fromCallable(() -> {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("email", user.getEmail());
            claims.put("name", user.getName());
            claims.put("lastname", user.getLastname());
            claims.put("rolId", user.getRolId());
            claims.put("roleName", Role.fromId(user.getRolId()).getName());

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        });
    }

    @Override
    public Mono<User> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                return User.builder()
                        .userId(Long.valueOf(claims.get("userId").toString()))
                        .email(claims.getSubject())
                        .name(claims.get("name", String.class))
                        .lastname(claims.get("lastname", String.class))
                        .rolId(Long.valueOf(claims.get("rolId").toString()))
                        .build();
            } catch (Exception e) {
                throw new InvalidTokenException("Token inválido");
            }
        });
    }
}
