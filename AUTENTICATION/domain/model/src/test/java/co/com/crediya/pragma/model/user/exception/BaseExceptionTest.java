package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseExceptionTest {

    @Test
    @DisplayName("Debe crear BaseException con todos los campos correctamente")
    void shouldCreateBaseExceptionWithAllFieldsCorrectly() {
        String message = "Error de prueba";
        String errorCode = "TEST_ERROR";
        String title = "Error de Prueba";
        int statusCode = 400;
        List<String> errors = List.of("Error 1", "Error 2");

        TestBaseException exception = new TestBaseException(message, errorCode, title, statusCode, errors);

        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(title, exception.getTitle());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
        assertNotNull(exception.getTimestamp());
        assertTrue(exception.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(exception.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    private static class TestBaseException extends BaseException {
        public TestBaseException(String message, String errorCode, String title, int statusCode, List<String> errors) {
            super(message, errorCode, title, statusCode, errors);
        }
    }
}
