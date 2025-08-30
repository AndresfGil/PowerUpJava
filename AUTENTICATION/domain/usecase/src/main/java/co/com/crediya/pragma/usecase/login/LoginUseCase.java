package co.com.crediya.pragma.usecase.login;


import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.exception.InvalidCredentialsException;
import co.com.crediya.pragma.model.user.exception.PasswordHashingException;
import co.com.crediya.pragma.model.user.gateways.AuthenticationGateway;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final AuthenticationGateway authenticationGateway;

    public Mono<String> login(String email, String password) {
        String normalizedEmail = email == null ? null : email.trim().toLowerCase();

        return userRepository.getUserByEmail(normalizedEmail)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException(normalizedEmail)))
                .flatMap(user -> validatePassword(user, password))
                .flatMap(authenticationGateway::generateToken);
    }

    private Mono<User> validatePassword(User user, String providedPassword) {
        String hashedProvidedPassword = hashPassword(providedPassword);

        if (user.getPassword() != null && user.getPassword().equals(hashedProvidedPassword)) {
            return Mono.just(user);
        }
        return Mono.error(new InvalidCredentialsException(user.getEmail()));
    }

    public Mono<User> getUserFromToken(String token) {
        return authenticationGateway.validateToken(token);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordHashingException("Error al hashear la contraseña", e);
        }
    }
}

