package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidTokenExceptionTest {

    @Test
    @DisplayName("Debe crear InvalidTokenException con mensaje")
    void shouldCreateInvalidTokenExceptionWithMessage() {
        String message = "Token inválido o expirado";
        
        InvalidTokenException exception = new InvalidTokenException(message);

        assertEquals(message, exception.getMessage());
        assertEquals("INVALID_TOKEN", exception.getErrorCode());
        assertEquals("Token invalido", exception.getTitle());
        assertEquals(401, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals(message, exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }



    @Test
    @DisplayName("Debe crear InvalidTokenException con mensaje vacío")
    void shouldCreateInvalidTokenExceptionWithEmptyMessage() {
        String message = "";
        
        InvalidTokenException exception = new InvalidTokenException(message);

        assertEquals(message, exception.getMessage());
        assertEquals("INVALID_TOKEN", exception.getErrorCode());
        assertEquals("Token invalido", exception.getTitle());
        assertEquals(401, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals(message, exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Debe verificar que es instancia de BaseException")
    void shouldVerifyIsInstanceOfBaseException() {
        InvalidTokenException exception = new InvalidTokenException("Test");
        
        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Debe verificar propiedades inmutables")
    void shouldVerifyImmutableProperties() {
        InvalidTokenException exception = new InvalidTokenException("Test");
        
        assertNotNull(exception.getErrorCode());
        assertNotNull(exception.getTitle());
        assertNotNull(exception.getErrors());
        assertNotNull(exception.getTimestamp());
        
        assertEquals("INVALID_TOKEN", exception.getErrorCode());
        assertEquals("Token invalido", exception.getTitle());
        assertEquals(401, exception.getStatusCode());
    }

    @Test
    @DisplayName("Debe verificar diferentes mensajes de error")
    void shouldVerifyDifferentErrorMessages() {
        InvalidTokenException exception1 = new InvalidTokenException("Token expirado");
        InvalidTokenException exception2 = new InvalidTokenException("Token malformado");
        InvalidTokenException exception3 = new InvalidTokenException("Token no encontrado");

        assertEquals("Token expirado", exception1.getMessage());
        assertEquals("Token malformado", exception2.getMessage());
        assertEquals("Token no encontrado", exception3.getMessage());

        assertEquals("Token expirado", exception1.getErrors().get(0));
        assertEquals("Token malformado", exception2.getErrors().get(0));
        assertEquals("Token no encontrado", exception3.getErrors().get(0));
    }

    @Test
    @DisplayName("Debe verificar que el timestamp se genera correctamente")
    void shouldVerifyTimestampIsGeneratedCorrectly() {
        InvalidTokenException exception1 = new InvalidTokenException("Test 1");
        
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        InvalidTokenException exception2 = new InvalidTokenException("Test 2");

        assertNotNull(exception1.getTimestamp());
        assertNotNull(exception2.getTimestamp());
        assertTrue(exception2.getTimestamp().isAfter(exception1.getTimestamp()) || 
                   exception2.getTimestamp().equals(exception1.getTimestamp()));
    }

}
