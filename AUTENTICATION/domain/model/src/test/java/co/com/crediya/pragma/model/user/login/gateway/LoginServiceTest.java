package co.com.crediya.pragma.model.user.login.gateway;

import co.com.crediya.pragma.model.user.login.AuthTokens;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    @Test
    void testLoginServiceInterface() {
        assertTrue(LoginService.class.isInterface());
    }

    @Test
    void testLoginMethodSignature() throws NoSuchMethodException {
        var method = LoginService.class.getMethod("login", String.class, String.class);
        assertEquals(Mono.class, method.getReturnType());
        assertEquals(2, method.getParameterCount());
        assertEquals(String.class, method.getParameterTypes()[0]);
        assertEquals(String.class, method.getParameterTypes()[1]);
    }

    @Test
    void testLoginServiceMethodsCount() {
        var methods = LoginService.class.getDeclaredMethods();
        assertEquals(1, methods.length);
    }

    @Test
    void testLoginMethodReturnType() throws NoSuchMethodException {
        var method = LoginService.class.getMethod("login", String.class, String.class);
        assertTrue(Mono.class.isAssignableFrom(method.getReturnType()));
    }
}
