package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailAlreadyExistsExceptionTest {

    @Test
    void testEmailAlreadyExistsExceptionCreation() {
        String email = "test@example.com";
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(email);

        assertEquals("El correo ya está registrado: test@example.com", exception.getMessage());
        assertEquals("EMAIL_DUPLICATE", exception.getErrorCode());
        assertEquals("Email duplicado", exception.getTitle());
        assertEquals(409, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El email test@example.com ya existe en el sistema", exception.getErrors().get(0));
    }

    @Test
    void testEmailAlreadyExistsExceptionWithNullEmail() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(null);

        assertEquals("El correo ya está registrado: null", exception.getMessage());
        assertEquals("EMAIL_DUPLICATE", exception.getErrorCode());
        assertEquals("Email duplicado", exception.getTitle());
        assertEquals(409, exception.getStatusCode());
    }

    @Test
    void testEmailAlreadyExistsExceptionWithEmptyEmail() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("");

        assertEquals("El correo ya está registrado: ", exception.getMessage());
        assertEquals("EMAIL_DUPLICATE", exception.getErrorCode());
        assertEquals("Email duplicado", exception.getTitle());
        assertEquals(409, exception.getStatusCode());
    }

    @Test
    void testEmailAlreadyExistsExceptionInheritance() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("test@example.com");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
