package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void testValidationExceptionCreation() {
        List<String> errors = List.of("Name is required", "Email is invalid");
        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
    }

    @Test
    void testValidationExceptionWithSingleError() {
        List<String> errors = List.of("Password is too short");
        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
    }

    @Test
    void testValidationExceptionWithEmptyErrors() {
        List<String> errors = List.of();
        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
    }

    @Test
    void testValidationExceptionWithNullErrors() {
        ValidationException exception = new ValidationException(null);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getErrors());
    }

    @Test
    void testValidationExceptionInheritance() {
        List<String> errors = List.of("Test error");
        ValidationException exception = new ValidationException(errors);

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
