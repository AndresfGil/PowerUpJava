package co.com.crediya.pragma.model.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testRoleBuilder() {
        Role role = Role.builder()
                .rolId(1L)
                .name("ADMIN")
                .description("Administrator role")
                .build();

        assertEquals(1L, role.getRolId());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrator role", role.getDescription());
    }

    @Test
    void testRoleConstructor() {
        Role role = new Role(2L, "USER", "Regular user role");

        assertEquals(2L, role.getRolId());
        assertEquals("USER", role.getName());
        assertEquals("Regular user role", role.getDescription());
    }

    @Test
    void testRoleSetters() {
        Role role = new Role();
        
        role.setRolId(3L);
        role.setName("MANAGER");
        role.setDescription("Manager role");

        assertEquals(3L, role.getRolId());
        assertEquals("MANAGER", role.getName());
        assertEquals("Manager role", role.getDescription());
    }

    @Test
    void testRoleWithNullValues() {
        Role role = new Role();
        
        assertNull(role.getRolId());
        assertNull(role.getName());
        assertNull(role.getDescription());
    }

    @Test
    void testRoleNoArgsConstructor() {
        Role role = new Role();
        
        assertNotNull(role);
        assertNull(role.getRolId());
        assertNull(role.getName());
        assertNull(role.getDescription());
    }

    @Test
    void testRoleAllArgsConstructor() {
        Role role = new Role(4L, "GUEST", "Guest user role");

        assertEquals(4L, role.getRolId());
        assertEquals("GUEST", role.getName());
        assertEquals("Guest user role", role.getDescription());
    }
}
