package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    @DisplayName("Debe crear ValidationException con lista de errores válida")
    void shouldCreateValidationExceptionWithValidErrorsList() {
        List<String> errors = Arrays.asList(
                "El nombre es obligatorio",
                "El email debe tener formato válido",
                "El salario debe ser mayor a 0"
        );

        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(3, exception.getErrors().size());
        assertTrue(exception.getErrors().contains("El nombre es obligatorio"));
        assertTrue(exception.getErrors().contains("El email debe tener formato válido"));
        assertTrue(exception.getErrors().contains("El salario debe ser mayor a 0"));
        assertNotNull(exception.getTimestamp());
    }


    @Test
    @DisplayName("Debe crear ValidationException con un solo error")
    void shouldCreateValidationExceptionWithSingleError() {
        List<String> errors = List.of("El email es obligatorio");

        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("El email es obligatorio", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }


    @Test
    @DisplayName("Debe heredar correctamente de BaseException")
    void shouldInheritCorrectlyFromBaseException() {
        List<String> errors = List.of("Error de prueba");

        ValidationException exception = new ValidationException(errors);

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
