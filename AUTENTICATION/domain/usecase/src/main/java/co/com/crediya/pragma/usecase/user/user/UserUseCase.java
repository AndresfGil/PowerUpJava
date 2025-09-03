package co.com.crediya.pragma.usecase.user.user;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.exception.EmailAlreadyExistsException;
import co.com.crediya.pragma.model.user.exception.PasswordHashingException;
import co.com.crediya.pragma.model.user.exception.UnauthorizedException;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        String normalized = user.getEmail() == null ? null : user.getEmail().trim().toLowerCase();

        User userWithHashedPassword = user.toBuilder()
                .email(normalized)
                .password(hashPassword(user.getPassword()))
                .build();

        return userRepository.getUserByEmail(normalized)
                .flatMap(existing -> Mono.<User>error(new EmailAlreadyExistsException(normalized)))
                .switchIfEmpty(userRepository.saveUser(userWithHashedPassword));
    }

    public Mono<User> saveUserWithAuthorization(User user, Long currentUserRoleId) {
        return validateUserCreationPermissions(currentUserRoleId)
                .then(saveUser(user));
    }

    private Mono<Void> validateUserCreationPermissions(Long currentUserRoleId) {
        if (currentUserRoleId == null || (currentUserRoleId != 1L && currentUserRoleId != 2L)) {
            return Mono.error(UnauthorizedException.insufficientPermissions("registrar usuarios"));
        }
        return Mono.empty();
    }

    public Flux<User> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public Mono<User> getUserByIdNumber(Long idNumber){
        return userRepository.getUserByIdNumber(idNumber);
    }

    public Mono<Void> deleteUser(Long idNumber){
        return userRepository.deleteUser(idNumber);
    }

    private String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new PasswordHashingException("La contraseña no puede ser nula o vacía", null);
        }

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
