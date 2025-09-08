package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RolNotFoundExceptionTest {

    @Test
    void testRolNotFoundExceptionDefaultConstructor() {
        RolNotFoundException exception = new RolNotFoundException();

        assertEquals("Rol no encontrado", exception.getMessage());
        assertEquals("ROLE_NOT_FOUND", exception.getErrorCode());
        assertEquals("Rol no identificado", exception.getTitle());
        assertEquals(404, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El rol especificado no existe", exception.getErrors().get(0));
    }

    @Test
    void testRolNotFoundExceptionWithRoleId() {
        String roleId = "123";
        RolNotFoundException exception = new RolNotFoundException(roleId);

        assertEquals("Rol no encontrado: 123", exception.getMessage());
        assertEquals("ROLE_NOT_FOUND", exception.getErrorCode());
        assertEquals("Rol no encontrado", exception.getTitle());
        assertEquals(404, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El rol con ID 123 no existe", exception.getErrors().get(0));
    }

    @Test
    void testRolNotFoundExceptionWithNullRoleId() {
        RolNotFoundException exception = new RolNotFoundException(null);

        assertEquals("Rol no encontrado: null", exception.getMessage());
        assertEquals("ROLE_NOT_FOUND", exception.getErrorCode());
        assertEquals("Rol no encontrado", exception.getTitle());
        assertEquals(404, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El rol con ID null no existe", exception.getErrors().get(0));
    }

    @Test
    void testRolNotFoundExceptionWithEmptyRoleId() {
        RolNotFoundException exception = new RolNotFoundException("");

        assertEquals("Rol no encontrado: ", exception.getMessage());
        assertEquals("ROLE_NOT_FOUND", exception.getErrorCode());
        assertEquals("Rol no encontrado", exception.getTitle());
        assertEquals(404, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El rol con ID  no existe", exception.getErrors().get(0));
    }

    @Test
    void testRolNotFoundExceptionInheritance() {
        RolNotFoundException exception = new RolNotFoundException();

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
