package co.com.crediya.pragma.usecase.login;

import co.com.crediya.pragma.model.user.login.AuthTokens;
import co.com.crediya.pragma.model.user.login.gateway.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private LoginService loginService;

    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        loginUseCase = new LoginUseCase(loginService);
    }

    @Test
    void testLoginSuccess() {
        String email = "test@example.com";
        String password = "password123";
        AuthTokens expectedTokens = new AuthTokens("token123", Instant.now().plusSeconds(3600), 60L);

        when(loginService.login(email, password)).thenReturn(Mono.just(expectedTokens));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectNext(expectedTokens)
                .verifyComplete();
    }

    @Test
    void testLoginFailure() {
        String email = "test@example.com";
        String password = "wrongpassword";

        when(loginService.login(email, password)).thenReturn(Mono.error(new RuntimeException("Invalid credentials")));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testLoginWithNullEmail() {
        String password = "password123";

        when(loginService.login(null, password)).thenReturn(Mono.error(new IllegalArgumentException("Email cannot be null")));

        StepVerifier.create(loginUseCase.login(null, password))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testLoginWithNullPassword() {
        String email = "test@example.com";

        when(loginService.login(email, null)).thenReturn(Mono.error(new IllegalArgumentException("Password cannot be null")));

        StepVerifier.create(loginUseCase.login(email, null))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testLoginWithEmptyEmail() {
        String email = "";
        String password = "password123";

        when(loginService.login(email, password)).thenReturn(Mono.error(new IllegalArgumentException("Email cannot be empty")));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testLoginWithEmptyPassword() {
        String email = "test@example.com";
        String password = "";

        when(loginService.login(email, password)).thenReturn(Mono.error(new IllegalArgumentException("Password cannot be empty")));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testLoginServiceCalledWithCorrectParameters() {
        String email = "test@example.com";
        String password = "password123";
        AuthTokens expectedTokens = new AuthTokens("token123", Instant.now().plusSeconds(3600), 60L);

        when(loginService.login(email, password)).thenReturn(Mono.just(expectedTokens));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectNext(expectedTokens)
                .verifyComplete();
    }
}
