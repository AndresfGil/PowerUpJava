package co.com.crediya.pragma.r2dbc.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    @DisplayName("Debe crear UserEntity con todos los campos correctamente")
    void shouldCreateUserEntityWithAllFieldsCorrectly() {
        UserEntity userEntity = UserEntity.builder()
                .userId(1L)
                .name("Juan")
                .lastname("Pérez")
                .email("juan.perez@test.com")
                .documentId("12345678")
                .address("Calle 123 #45-67")
                .birthDate("1990-01-01")
                .phone("3001234567")
                .rolId(1L)
                .baseSalary(2000000.0)
                .build();

        assertEquals(1L, userEntity.getUserId());
        assertEquals("Juan", userEntity.getName());
        assertEquals("Pérez", userEntity.getLastname());
        assertEquals("juan.perez@test.com", userEntity.getEmail());
        assertEquals("12345678", userEntity.getDocumentId());
        assertEquals("Calle 123 #45-67", userEntity.getAddress());
        assertEquals("1990-01-01", userEntity.getBirthDate());
        assertEquals("3001234567", userEntity.getPhone());
        assertEquals(1L, userEntity.getRolId());
        assertEquals(2000000.0, userEntity.getBaseSalary());
    }

}
