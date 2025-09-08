package co.com.crediya.pragma.model.user.login;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class AuthTokensTest {

    @Test
    void testAuthTokensCreation() {
        Instant expiresAt = Instant.now().plusSeconds(3600);
        AuthTokens authTokens = new AuthTokens("token123", expiresAt, 60L);

        assertEquals("token123", authTokens.accessToken());
        assertEquals(expiresAt, authTokens.expiresAt());
        assertEquals(60L, authTokens.ttlMinutes());
    }

    @Test
    void testAuthTokensWithNullValues() {
        AuthTokens authTokens = new AuthTokens(null, null, null);

        assertNull(authTokens.accessToken());
        assertNull(authTokens.expiresAt());
        assertNull(authTokens.ttlMinutes());
    }

    @Test
    void testAuthTokensEquality() {
        Instant expiresAt = Instant.now().plusSeconds(3600);
        AuthTokens authTokens1 = new AuthTokens("token123", expiresAt, 60L);
        AuthTokens authTokens2 = new AuthTokens("token123", expiresAt, 60L);

        assertEquals(authTokens1, authTokens2);
        assertEquals(authTokens1.hashCode(), authTokens2.hashCode());
    }

    @Test
    void testAuthTokensToString() {
        Instant expiresAt = Instant.now().plusSeconds(3600);
        AuthTokens authTokens = new AuthTokens("token123", expiresAt, 60L);

        String toString = authTokens.toString();
        assertTrue(toString.contains("token123"));
        assertTrue(toString.contains("60"));
    }

    @Test
    void testAuthTokensWithZeroTtl() {
        Instant expiresAt = Instant.now();
        AuthTokens authTokens = new AuthTokens("token456", expiresAt, 0L);

        assertEquals("token456", authTokens.accessToken());
        assertEquals(expiresAt, authTokens.expiresAt());
        assertEquals(0L, authTokens.ttlMinutes());
    }

    @Test
    void testAuthTokensWithNegativeTtl() {
        Instant expiresAt = Instant.now().minusSeconds(3600);
        AuthTokens authTokens = new AuthTokens("token789", expiresAt, -60L);

        assertEquals("token789", authTokens.accessToken());
        assertEquals(expiresAt, authTokens.expiresAt());
        assertEquals(-60L, authTokens.ttlMinutes());
    }
}
