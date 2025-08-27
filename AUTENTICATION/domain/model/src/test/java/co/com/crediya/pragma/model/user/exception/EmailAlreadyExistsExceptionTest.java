package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Debe crear EmailAlreadyExistsException con email válido")
    void shouldCreateEmailAlreadyExistsExceptionWithValidEmail() {
        String email = "test@example.com";

        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(email);

        assertEquals("El correo ya está registrado: " + email, exception.getMessage());
        assertEquals("EMAIL_DUPLICATE", exception.getErrorCode());
        assertEquals("Email duplicado", exception.getTitle());
        assertEquals(409, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El email " + email + " ya existe en el sistema", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Debe crear EmailAlreadyExistsException con email null")
    void shouldCreateEmailAlreadyExistsExceptionWithNullEmail() {
        String email = null;

        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(email);

        assertEquals("El correo ya está registrado: " + email, exception.getMessage());
        assertEquals("EMAIL_DUPLICATE", exception.getErrorCode());
        assertEquals("Email duplicado", exception.getTitle());
        assertEquals(409, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El email " + email + " ya existe en el sistema", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }


    @Test
    @DisplayName("Debe heredar correctamente de BaseException")
    void shouldInheritCorrectlyFromBaseException() {
        String email = "test@example.com";

        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(email);

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
