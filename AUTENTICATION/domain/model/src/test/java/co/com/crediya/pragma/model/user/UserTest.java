package co.com.crediya.pragma.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Debe crear un usuario con todos los campos correctamente")
    void shouldCreateUserWithAllFieldsCorrectly() {
        User user = User.builder()
                .userId(1L)
                .name("Juan")
                .lastname("Pérez")
                .email("juan.perez@test.com")
                .documentId("12345678")
                .address("Calle 123 #45-67")
                .birthDate("1990-01-01")
                .phone("3001234567")
                .rolId(1L)
                .baseSalary(BigDecimal.valueOf(2000000))
                .build();

        assertEquals(1L, user.getUserId());
        assertEquals("Juan", user.getName());
        assertEquals("Pérez", user.getLastname());
        assertEquals("juan.perez@test.com", user.getEmail());
        assertEquals("12345678", user.getDocumentId());
        assertEquals("Calle 123 #45-67", user.getAddress());
        assertEquals("1990-01-01", user.getBirthDate());
        assertEquals("3001234567", user.getPhone());
        assertEquals(1L, user.getRolId());
        assertEquals(BigDecimal.valueOf(2000000), user.getBaseSalary());
    }

}
