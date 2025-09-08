package co.com.crediya.pragma.model.user.solicitudes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserSimpleViewTest {

    @Test
    void testUserSimpleViewInterface() {
        assertTrue(UserSimpleView.class.isInterface());
    }

    @Test
    void testGetNameMethodSignature() throws NoSuchMethodException {
        var method = UserSimpleView.class.getMethod("getName");
        assertEquals(String.class, method.getReturnType());
        assertEquals(0, method.getParameterCount());
    }

    @Test
    void testGetBaseSalaryMethodSignature() throws NoSuchMethodException {
        var method = UserSimpleView.class.getMethod("getBaseSalary");
        assertEquals(Long.class, method.getReturnType());
        assertEquals(0, method.getParameterCount());
    }

    @Test
    void testGetEmailMethodSignature() throws NoSuchMethodException {
        var method = UserSimpleView.class.getMethod("getEmail");
        assertEquals(String.class, method.getReturnType());
        assertEquals(0, method.getParameterCount());
    }

    @Test
    void testUserSimpleViewMethodsCount() {
        var methods = UserSimpleView.class.getDeclaredMethods();
        assertEquals(3, methods.length);
    }
}
