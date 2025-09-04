package co.com.crediya.pragma.model.user.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedExceptionTest {

    @Test
    @DisplayName("Debe crear UnauthorizedException con mensaje")
    void shouldCreateUnauthorizedExceptionWithMessage() {
        String message = "No autorizado para realizar esta acción";
        
        UnauthorizedException exception = new UnauthorizedException(message);

        assertEquals(message, exception.getMessage());
        assertEquals("UNAUTHORIZED", exception.getErrorCode());
        assertEquals("No autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals(message, exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }


    @Test
    @DisplayName("Debe crear UnauthorizedException usando método estático insufficientPermissions")
    void shouldCreateUnauthorizedExceptionUsingInsufficientPermissions() {
        String action = "registrar usuarios";
        
        UnauthorizedException exception = UnauthorizedException.insufficientPermissions(action);

        assertEquals("Permisos insuficientes para realizar la acción: " + action, exception.getMessage());
        assertEquals("UNAUTHORIZED", exception.getErrorCode());
        assertEquals("No autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Permisos insuficientes para realizar la acción: " + action, exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Debe crear UnauthorizedException usando método estático invalidToken")
    void shouldCreateUnauthorizedExceptionUsingInvalidToken() {
        UnauthorizedException exception = UnauthorizedException.invalidToken();

        assertEquals("Token de autenticación inválido o expirado", exception.getMessage());
        assertEquals("UNAUTHORIZED", exception.getErrorCode());
        assertEquals("No autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Token de autenticación inválido o expirado", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Debe crear UnauthorizedException con acción nula")
    void shouldCreateUnauthorizedExceptionWithNullAction() {
        UnauthorizedException exception = UnauthorizedException.insufficientPermissions(null);

        assertEquals("Permisos insuficientes para realizar la acción: null", exception.getMessage());
        assertEquals("UNAUTHORIZED", exception.getErrorCode());
        assertEquals("No autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Permisos insuficientes para realizar la acción: null", exception.getErrors().get(0));
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Debe verificar que es instancia de BaseException")
    void shouldVerifyIsInstanceOfBaseException() {
        UnauthorizedException exception = new UnauthorizedException("Test");
        
        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Debe verificar propiedades inmutables")
    void shouldVerifyImmutableProperties() {
        UnauthorizedException exception = new UnauthorizedException("Test");
        
        assertNotNull(exception.getErrorCode());
        assertNotNull(exception.getTitle());
        assertNotNull(exception.getErrors());
        assertNotNull(exception.getTimestamp());
        
        assertEquals("UNAUTHORIZED", exception.getErrorCode());
        assertEquals("No autorizado", exception.getTitle());
        assertEquals(403, exception.getStatusCode());
    }

    @Test
    @DisplayName("Debe verificar diferentes mensajes de error")
    void shouldVerifyDifferentErrorMessages() {
        UnauthorizedException exception1 = new UnauthorizedException("Acceso denegado");
        UnauthorizedException exception2 = UnauthorizedException.insufficientPermissions("eliminar usuarios");
        UnauthorizedException exception3 = UnauthorizedException.invalidToken();

        assertEquals("Acceso denegado", exception1.getMessage());
        assertEquals("Permisos insuficientes para realizar la acción: eliminar usuarios", exception2.getMessage());
        assertEquals("Token de autenticación inválido o expirado", exception3.getMessage());

        assertEquals("Acceso denegado", exception1.getErrors().get(0));
        assertEquals("Permisos insuficientes para realizar la acción: eliminar usuarios", exception2.getErrors().get(0));
        assertEquals("Token de autenticación inválido o expirado", exception3.getErrors().get(0));
    }

}
