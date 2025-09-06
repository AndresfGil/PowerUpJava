package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BaseExceptionTest {

    @Test
    void testBaseExceptionCreation() {
        List<String> errors = List.of("Error 1", "Error 2");
        BaseException exception = new TestBaseException("Test message", "TEST_CODE", "Test Title", 400, errors);

        assertEquals("Test message", exception.getMessage());
        assertEquals("TEST_CODE", exception.getErrorCode());
        assertEquals("Test Title", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
        assertNotNull(exception.getTimestamp());
        assertTrue(exception.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testBaseExceptionWithNullErrors() {
        BaseException exception = new TestBaseException("Test message", "TEST_CODE", "Test Title", 400, null);

        assertEquals("Test message", exception.getMessage());
        assertEquals("TEST_CODE", exception.getErrorCode());
        assertEquals("Test Title", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getErrors());
        assertNotNull(exception.getTimestamp());
    }

    @Test
    void testBaseExceptionWithEmptyErrors() {
        List<String> emptyErrors = List.of();
        BaseException exception = new TestBaseException("Test message", "TEST_CODE", "Test Title", 400, emptyErrors);

        assertEquals("Test message", exception.getMessage());
        assertEquals("TEST_CODE", exception.getErrorCode());
        assertEquals("Test Title", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(emptyErrors, exception.getErrors());
        assertNotNull(exception.getTimestamp());
    }

    @Test
    void testBaseExceptionTimestamp() {
        LocalDateTime beforeCreation = LocalDateTime.now();
        BaseException exception = new TestBaseException("Test message", "TEST_CODE", "Test Title", 400, null);
        LocalDateTime afterCreation = LocalDateTime.now();

        assertTrue(exception.getTimestamp().isAfter(beforeCreation.minusSeconds(1)));
        assertTrue(exception.getTimestamp().isBefore(afterCreation.plusSeconds(1)));
    }

    private static class TestBaseException extends BaseException {
        public TestBaseException(String message, String errorCode, String title, int statusCode, List<String> errors) {
            super(message, errorCode, title, statusCode, errors);
        }
    }
}
