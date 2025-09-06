package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedExceptionTest {

    @Test
    void testUnauthorizedExceptionCreation() {
        String message = "Access denied";
        UnauthorizedException exception = new UnauthorizedException(message);

        assertEquals("Access denied", exception.getMessage());
        assertEquals("UNAUTHORIZED", exception.getErrorCode());
        assertEquals("No autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Access denied", exception.getErrors().get(0));
    }


    @Test
    void testUnauthorizedExceptionInheritance() {
        UnauthorizedException exception = new UnauthorizedException("Access denied");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }
}
