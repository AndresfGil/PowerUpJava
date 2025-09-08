package co.com.crediya.pragma.api.security;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.exception.InvalidCredentialsException;
import co.com.crediya.pragma.model.user.gateways.RoleRepository;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.model.user.login.AuthTokens;
import co.com.crediya.pragma.model.user.login.gateway.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class LoginServiceImple implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImple.class);
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final JwtService jwt;


    public LoginServiceImple(UserRepository users, RoleRepository roles, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
        this.jwt = jwt;
    }


    public Mono<AuthTokens> login(String email, String password) {
        return users.getUserByEmail(email)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException(email)))
                .flatMap(u -> validatePassword(u, password))
                .zipWhen(u -> roles.findById(u.getRolId())
                        .switchIfEmpty(Mono.error(new RuntimeException("Rol no encontrado para el usuario: " + email)))
                        .doOnSuccess(rol -> log.info(" Login OK email={} rol={}", email, rol.getName()))
                )
                .map(tuple -> {
                    var user = tuple.getT1(); // usuario
                    var rol = tuple.getT2();  // rol
                    String token = jwt.generate(
                            String.valueOf(user.getDocumentId()),
                            user.getEmail(),
                            Set.of(rol.getName())
                    );

                    log.info(" [LOGIN-OK]  email={} roles={}", email, rol.getName());
                    return new AuthTokens(token, jwt.expiresAt(), jwt.getttlMinutes());

                })
                .doOnError(e -> log.warn(" [LOGIN-FAIL] email={} err={}", email, e.getMessage()));

    }

    private Mono<User> validatePassword(User u, String raw) {
        if (!encoder.matches(raw, u.getPassword())) return Mono.error(new InvalidCredentialsException(u.getEmail()));
        return Mono.just(u);
    }
}
