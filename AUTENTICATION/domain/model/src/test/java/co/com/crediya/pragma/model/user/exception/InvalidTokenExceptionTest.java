package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidTokenExceptionTest {

    @Test
    void testInvalidTokenExceptionCreation() {
        String message = "Token expired";
        InvalidTokenException exception = new InvalidTokenException(message);

        assertEquals("Token expired", exception.getMessage());
        assertEquals("INVALID_TOKEN", exception.getErrorCode());
        assertEquals("Token invalido", exception.getTitle());
        assertEquals(401, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Token expired", exception.getErrors().get(0));
    }

    @Test
    void testInvalidTokenExceptionWithEmptyMessage() {
        InvalidTokenException exception = new InvalidTokenException("");

        assertEquals("", exception.getMessage());
        assertEquals("INVALID_TOKEN", exception.getErrorCode());
        assertEquals("Token invalido", exception.getTitle());
        assertEquals(401, exception.getStatusCode());
    }

    @Test
    void testInvalidTokenExceptionInheritance() {
        InvalidTokenException exception = new InvalidTokenException("Token expired");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
