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
                .password("hashedPassword")
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
        assertEquals("hashedPassword", user.getPassword());
        assertEquals("12345678", user.getDocumentId());
        assertEquals("Calle 123 #45-67", user.getAddress());
        assertEquals("1990-01-01", user.getBirthDate());
        assertEquals("3001234567", user.getPhone());
        assertEquals(1L, user.getRolId());
        assertEquals(BigDecimal.valueOf(2000000), user.getBaseSalary());
    }

    @Test
    @DisplayName("Debe crear un usuario con campos nulos")
    void shouldCreateUserWithNullFields() {
        User user = User.builder()
                .userId(null)
                .name(null)
                .lastname(null)
                .email(null)
                .password(null)
                .documentId(null)
                .address(null)
                .birthDate(null)
                .phone(null)
                .rolId(null)
                .baseSalary(null)
                .build();

        assertNull(user.getUserId());
        assertNull(user.getName());
        assertNull(user.getLastname());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getDocumentId());
        assertNull(user.getAddress());
        assertNull(user.getBirthDate());
        assertNull(user.getPhone());
        assertNull(user.getRolId());
        assertNull(user.getBaseSalary());
    }

    @Test
    @DisplayName("Debe crear un usuario usando constructor por defecto")
    void shouldCreateUserWithDefaultConstructor() {
        User user = new User();

        assertNull(user.getUserId());
        assertNull(user.getName());
        assertNull(user.getLastname());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getDocumentId());
        assertNull(user.getAddress());
        assertNull(user.getBirthDate());
        assertNull(user.getPhone());
        assertNull(user.getRolId());
        assertNull(user.getBaseSalary());
    }

    @Test
    @DisplayName("Debe crear un usuario usando constructor con todos los parámetros")
    void shouldCreateUserWithAllArgsConstructor() {
        User user = new User(
                1L,
                "Juan",
                "Pérez",
                "juan.perez@test.com",
                "hashedPassword",
                "12345678",
                "Calle 123",
                "1990-01-01",
                "3001234567",
                1L,
                BigDecimal.valueOf(2000000)
        );

        assertEquals(1L, user.getUserId());
        assertEquals("Juan", user.getName());
        assertEquals("Pérez", user.getLastname());
        assertEquals("juan.perez@test.com", user.getEmail());
        assertEquals("hashedPassword", user.getPassword());
        assertEquals("12345678", user.getDocumentId());
        assertEquals("Calle 123", user.getAddress());
        assertEquals("1990-01-01", user.getBirthDate());
        assertEquals("3001234567", user.getPhone());
        assertEquals(1L, user.getRolId());
        assertEquals(BigDecimal.valueOf(2000000), user.getBaseSalary());
    }

    @Test
    @DisplayName("Debe modificar campos usando setters")
    void shouldModifyFieldsUsingSetters() {
        User user = new User();
        
        user.setUserId(2L);
        user.setName("María");
        user.setLastname("García");
        user.setEmail("maria.garcia@test.com");
        user.setPassword("newHashedPassword");
        user.setDocumentId("87654321");
        user.setAddress("Calle 456");
        user.setBirthDate("1985-05-15");
        user.setPhone("3007654321");
        user.setRolId(2L);
        user.setBaseSalary(BigDecimal.valueOf(2500000));

        assertEquals(2L, user.getUserId());
        assertEquals("María", user.getName());
        assertEquals("García", user.getLastname());
        assertEquals("maria.garcia@test.com", user.getEmail());
        assertEquals("newHashedPassword", user.getPassword());
        assertEquals("87654321", user.getDocumentId());
        assertEquals("Calle 456", user.getAddress());
        assertEquals("1985-05-15", user.getBirthDate());
        assertEquals("3007654321", user.getPhone());
        assertEquals(2L, user.getRolId());
        assertEquals(BigDecimal.valueOf(2500000), user.getBaseSalary());
    }

    @Test
    @DisplayName("Debe crear copia usando toBuilder")
    void shouldCreateCopyUsingToBuilder() {
        User originalUser = User.builder()
                .userId(1L)
                .name("Juan")
                .lastname("Pérez")
                .email("juan.perez@test.com")
                .password("hashedPassword")
                .documentId("12345678")
                .address("Calle 123")
                .birthDate("1990-01-01")
                .phone("3001234567")
                .rolId(1L)
                .baseSalary(BigDecimal.valueOf(2000000))
                .build();

        User modifiedUser = originalUser.toBuilder()
                .name("María")
                .email("maria.perez@test.com")
                .baseSalary(BigDecimal.valueOf(3000000))
                .build();

        assertEquals(1L, modifiedUser.getUserId());
        assertEquals("María", modifiedUser.getName());
        assertEquals("Pérez", modifiedUser.getLastname());
        assertEquals("maria.perez@test.com", modifiedUser.getEmail());
        assertEquals("hashedPassword", modifiedUser.getPassword());
        assertEquals("12345678", modifiedUser.getDocumentId());
        assertEquals("Calle 123", modifiedUser.getAddress());
        assertEquals("1990-01-01", modifiedUser.getBirthDate());
        assertEquals("3001234567", modifiedUser.getPhone());
        assertEquals(1L, modifiedUser.getRolId());
        assertEquals(BigDecimal.valueOf(3000000), modifiedUser.getBaseSalary());

        assertEquals("Juan", originalUser.getName());
        assertEquals("juan.perez@test.com", originalUser.getEmail());
        assertEquals(BigDecimal.valueOf(2000000), originalUser.getBaseSalary());
    }

    @Test
    @DisplayName("Debe manejar diferentes tipos de roles")
    void shouldHandleDifferentRoleTypes() {
        User adminUser = User.builder().rolId(1L).build();
        User asesorUser = User.builder().rolId(2L).build();
        User clienteUser = User.builder().rolId(3L).build();

        assertEquals(1L, adminUser.getRolId());
        assertEquals(2L, asesorUser.getRolId());
        assertEquals(3L, clienteUser.getRolId());
    }

    @Test
    @DisplayName("Debe manejar diferentes valores de salario")
    void shouldHandleDifferentSalaryValues() {
        User userWithZeroSalary = User.builder().baseSalary(BigDecimal.ZERO).build();
        User userWithHighSalary = User.builder().baseSalary(BigDecimal.valueOf(10000000)).build();
        User userWithDecimalSalary = User.builder().baseSalary(BigDecimal.valueOf(2500000.50)).build();

        assertEquals(BigDecimal.ZERO, userWithZeroSalary.getBaseSalary());
        assertEquals(BigDecimal.valueOf(10000000), userWithHighSalary.getBaseSalary());
        assertEquals(BigDecimal.valueOf(2500000.50), userWithDecimalSalary.getBaseSalary());
    }

}
