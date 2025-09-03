package co.com.crediya.pragma.api.helper;

import co.com.crediya.pragma.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityContextHelperTest {

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private SecurityContextHelper securityContextHelper;

    @BeforeEach
    void setUp() {
        securityContextHelper = new SecurityContextHelper();
    }

    @Test
    void getCurrentUser_WhenValidContext_ShouldReturnUser() {
        // Arrange
        User user = User.builder()
                .userId(1L)
                .email("test@example.com")
                .name("Test")
                .lastname("User")
                .rolId(1L)
                .build();

        UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(user, null, null);

        when(exchange.getAttribute(SecurityContext.class.getName())).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        // Act & Assert
        StepVerifier.create(securityContextHelper.getCurrentUser(exchange))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void getCurrentUser_WhenNoContext_ShouldReturnError() {
        // Arrange
        when(exchange.getAttribute(SecurityContext.class.getName())).thenReturn(null);

        // Act & Assert
        StepVerifier.create(securityContextHelper.getCurrentUser(exchange))
                .expectError(RuntimeException.class)
                .verify();
    }
}
