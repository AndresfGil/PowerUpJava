package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashingExceptionTest {


    @Test
    @DisplayName("Debe crear PasswordHashingException con mensaje y causa nula")
    void shouldCreatePasswordHashingExceptionWithMessageAndNullCause() {
        String message = "Error al hashear la contraseña";
        
        PasswordHashingException exception = new PasswordHashingException(message, null);

        assertEquals(message, exception.getMessage());
        assertEquals("PASSWORD_HASHING_ERROR", exception.getErrorCode());
        assertEquals("Error al procesar contraseña", exception.getTitle());
        assertEquals(500, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Error interno al procesar la contraseña del usuario", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Debe crear PasswordHashingException con mensaje nulo")
    void shouldCreatePasswordHashingExceptionWithNullMessage() {
        PasswordHashingException exception = new PasswordHashingException(null, null);

        assertNull(exception.getMessage());
        assertEquals("PASSWORD_HASHING_ERROR", exception.getErrorCode());
        assertEquals("Error al procesar contraseña", exception.getTitle());
        assertEquals(500, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Error interno al procesar la contraseña del usuario", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Debe verificar que es instancia de BaseException")
    void shouldVerifyIsInstanceOfBaseException() {
        PasswordHashingException exception = new PasswordHashingException("Test", null);
        
        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Debe verificar propiedades inmutables")
    void shouldVerifyImmutableProperties() {
        PasswordHashingException exception = new PasswordHashingException("Test", null);
        
        assertNotNull(exception.getErrorCode());
        assertNotNull(exception.getTitle());
        assertNotNull(exception.getErrors());
        assertNotNull(exception.getTimestamp());
        
        assertEquals("PASSWORD_HASHING_ERROR", exception.getErrorCode());
        assertEquals("Error al procesar contraseña", exception.getTitle());
        assertEquals(500, exception.getStatusCode());
    }

}
