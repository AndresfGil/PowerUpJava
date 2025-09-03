package co.com.crediya.pragma.api.config;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.usecase.login.LoginUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private WebFilterChain chain;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(loginUseCase);
        when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void filter_WhenSwaggerPath_ShouldSkipJwtValidation() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/swagger-ui/index.html")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
        verify(loginUseCase, never()).getUserFromToken(any());
    }

    @Test
    void filter_WhenLoginPath_ShouldSkipJwtValidation() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .post("/api/v1/login")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
        verify(loginUseCase, never()).getUserFromToken(any());
    }

    @Test
    void filter_WhenApiPath_ShouldProcessJwtToken() {
        // Arrange
        User user = User.builder()
                .userId(1L)
                .email("test@example.com")
                .name("Test")
                .rolId(1L)
                .build();

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid.token.here")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(loginUseCase.getUserFromToken("valid.token.here")).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(loginUseCase).getUserFromToken("valid.token.here");
        verify(chain).filter(exchange);
    }
}
