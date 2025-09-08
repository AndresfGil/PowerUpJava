package co.com.crediya.pragma.model.user;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserBuilder() {
        User user = User.builder()
                .userId(1L)
                .name("John")
                .lastname("Doe")
                .email("john.doe@test.com")
                .password("password123")
                .documentId("12345678")
                .address("Test Address")
                .birthDate("1990-01-01")
                .phone("1234567890")
                .rolId(1L)
                .baseSalary(new BigDecimal("50000"))
                .build();

        assertEquals(1L, user.getUserId());
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastname());
        assertEquals("john.doe@test.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("12345678", user.getDocumentId());
        assertEquals("Test Address", user.getAddress());
        assertEquals("1990-01-01", user.getBirthDate());
        assertEquals("1234567890", user.getPhone());
        assertEquals(1L, user.getRolId());
        assertEquals(new BigDecimal("50000"), user.getBaseSalary());
    }

    @Test
    void testUserConstructor() {
        User user = new User(1L, "john.doe@test.com", "password123", "John", "Doe", 
                1L, "12345678", new BigDecimal("50000"), "Test Address", "1234567890", "1990-01-01");

        assertEquals(1L, user.getUserId());
        assertEquals("john.doe@test.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastname());
        assertEquals(1L, user.getRolId());
        assertEquals("12345678", user.getDocumentId());
        assertEquals(new BigDecimal("50000"), user.getBaseSalary());
        assertEquals("Test Address", user.getAddress());
        assertEquals("1234567890", user.getPhone());
        assertEquals("1990-01-01", user.getBirthDate());
    }

    @Test
    void testUserSetters() {
        User user = new User();
        
        user.setUserId(2L);
        user.setName("Jane");
        user.setLastname("Smith");
        user.setEmail("jane.smith@test.com");
        user.setPassword("password456");
        user.setDocumentId("87654321");
        user.setAddress("Another Address");
        user.setBirthDate("1995-05-15");
        user.setPhone("0987654321");
        user.setRolId(2L);
        user.setBaseSalary(new BigDecimal("60000"));

        assertEquals(2L, user.getUserId());
        assertEquals("Jane", user.getName());
        assertEquals("Smith", user.getLastname());
        assertEquals("jane.smith@test.com", user.getEmail());
        assertEquals("password456", user.getPassword());
        assertEquals("87654321", user.getDocumentId());
        assertEquals("Another Address", user.getAddress());
        assertEquals("1995-05-15", user.getBirthDate());
        assertEquals("0987654321", user.getPhone());
        assertEquals(2L, user.getRolId());
        assertEquals(new BigDecimal("60000"), user.getBaseSalary());
    }

    @Test
    void testUserToBuilder() {
        User originalUser = User.builder()
                .userId(1L)
                .name("John")
                .email("john@test.com")
                .build();

        User modifiedUser = originalUser.toBuilder()
                .name("Johnny")
                .build();

        assertEquals(1L, modifiedUser.getUserId());
        assertEquals("Johnny", modifiedUser.getName());
        assertEquals("john@test.com", modifiedUser.getEmail());
    }

    @Test
    void testUserWithNullValues() {
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
}
