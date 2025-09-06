package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidCredentialsExceptionTest {

    @Test
    void testInvalidCredentialsExceptionCreation() {
        String email = "test@example.com";
        InvalidCredentialsException exception = new InvalidCredentialsException(email);

        assertEquals("Credenciales inválidas para el email: test@example.com", exception.getMessage());
        assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
        assertEquals("Credenciales inválidas", exception.getTitle());
        assertEquals(401, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El email o contraseña proporcionados son incorrectos", exception.getErrors().get(0));
    }

    @Test
    void testInvalidCredentialsExceptionWithNullEmail() {
        InvalidCredentialsException exception = new InvalidCredentialsException(null);

        assertEquals("Credenciales inválidas para el email: null", exception.getMessage());
        assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
        assertEquals("Credenciales inválidas", exception.getTitle());
        assertEquals(401, exception.getStatusCode());
    }

    @Test
    void testInvalidCredentialsExceptionWithEmptyEmail() {
        InvalidCredentialsException exception = new InvalidCredentialsException("");

        assertEquals("Credenciales inválidas para el email: ", exception.getMessage());
        assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
        assertEquals("Credenciales inválidas", exception.getTitle());
        assertEquals(401, exception.getStatusCode());
    }

    @Test
    void testInvalidCredentialsExceptionInheritance() {
        InvalidCredentialsException exception = new InvalidCredentialsException("test@example.com");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
