package co.com.crediya.pragma.usecase.login;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.exception.InvalidCredentialsException;
import co.com.crediya.pragma.model.user.gateways.AuthenticationGateway;
import co.com.crediya.pragma.model.user.gateways.UserRepository;
import co.com.crediya.pragma.usecase.login.LoginUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationGateway authenticationGateway;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId(1L)
                .name("Juan")
                .lastname("Pérez")
                .email("juan.perez@test.com")
                .password("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8")
                .documentId("12345678")
                .rolId(3L)
                .baseSalary(BigDecimal.valueOf(2000000))
                .build();
    }

    @Test
    @DisplayName("Debe hacer login exitosamente con credenciales válidas")
    void shouldLoginSuccessfullyWithValidCredentials() {
        String email = "juan.perez@test.com";
        String password = "password";
        String token = "jwt.token.here";

        when(userRepository.getUserByEmail(email)).thenReturn(Mono.just(testUser));
        when(authenticationGateway.generateToken(any(User.class))).thenReturn(Mono.just(token));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectNext(token)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar InvalidCredentialsException cuando el usuario no existe")
    void shouldThrowInvalidCredentialsExceptionWhenUserNotFound() {
        String email = "nonexistent@test.com";
        String password = "password";

        when(userRepository.getUserByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.login(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar InvalidCredentialsException cuando la contraseña es incorrecta")
    void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
        String email = "juan.perez@test.com";
        String wrongPassword = "wrongpassword";

        when(userRepository.getUserByEmail(email)).thenReturn(Mono.just(testUser));

        StepVerifier.create(loginUseCase.login(email, wrongPassword))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe obtener usuario del token exitosamente")
    void shouldGetUserFromTokenSuccessfully() {
        String token = "valid.jwt.token";

        when(authenticationGateway.validateToken(token)).thenReturn(Mono.just(testUser));

        StepVerifier.create(loginUseCase.getUserFromToken(token))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe normalizar el email a minúsculas")
    void shouldNormalizeEmailToLowerCase() {
        String email = "JUAN.PEREZ@TEST.COM";
        String password = "password";
        String token = "jwt.token.here";

        when(userRepository.getUserByEmail("juan.perez@test.com")).thenReturn(Mono.just(testUser));
        when(authenticationGateway.generateToken(any(User.class))).thenReturn(Mono.just(token));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectNext(token)
                .verifyComplete();
    }
}
