package co.com.crediya.pragma.model.user.gateways;

import co.com.crediya.pragma.model.user.User;
import co.com.crediya.pragma.model.user.solicitudes.UserSimpleView;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void testUserRepositoryInterface() {
        assertTrue(UserRepository.class.isInterface());
    }

    @Test
    void testSaveUserMethodSignature() throws NoSuchMethodException {
        var method = UserRepository.class.getMethod("saveUser", User.class);
        assertEquals(Mono.class, method.getReturnType());
        assertEquals(1, method.getParameterCount());
        assertEquals(User.class, method.getParameterTypes()[0]);
    }

    @Test
    void testGetUserByEmailMethodSignature() throws NoSuchMethodException {
        var method = UserRepository.class.getMethod("getUserByEmail", String.class);
        assertEquals(Mono.class, method.getReturnType());
        assertEquals(1, method.getParameterCount());
        assertEquals(String.class, method.getParameterTypes()[0]);
    }

    @Test
    void testGetUsersByEmailsMethodSignature() throws NoSuchMethodException {
        var method = UserRepository.class.getMethod("getUsersByEmails", List.class);
        assertEquals(Flux.class, method.getReturnType());
        assertEquals(1, method.getParameterCount());
        assertEquals(List.class, method.getParameterTypes()[0]);
    }

    @Test
    void testUserRepositoryMethodsCount() {
        var methods = UserRepository.class.getDeclaredMethods();
        assertEquals(3, methods.length);
    }
}
