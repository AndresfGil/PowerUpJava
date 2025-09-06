package co.com.crediya.pragma.model.user.gateways;

import co.com.crediya.pragma.model.user.Role;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryTest {

    @Test
    void testRoleRepositoryInterface() {
        assertTrue(RoleRepository.class.isInterface());
    }

    @Test
    void testFindByIdMethodSignature() throws NoSuchMethodException {
        var method = RoleRepository.class.getMethod("findById", Long.class);
        assertEquals(Mono.class, method.getReturnType());
        assertEquals(1, method.getParameterCount());
        assertEquals(Long.class, method.getParameterTypes()[0]);
    }

    @Test
    void testRoleRepositoryMethodsCount() {
        var methods = RoleRepository.class.getDeclaredMethods();
        assertEquals(1, methods.length);
    }
}
