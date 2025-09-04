package co.com.crediya.pragma.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {


    @Test
    @DisplayName("Debe obtener rol ASESOR correctamente")
    void shouldGetAsesorRoleCorrectly() {
        Role asesor = Role.ASESOR;

        assertEquals(2L, asesor.getId());
        assertEquals("ASESOR", asesor.getName());
    }

    @Test
    @DisplayName("Debe obtener rol CLIENTE correctamente")
    void shouldGetClienteRoleCorrectly() {
        Role cliente = Role.CLIENTE;

        assertEquals(3L, cliente.getId());
        assertEquals("CLIENTE", cliente.getName());
    }

    @Test
    @DisplayName("Debe obtener rol por ID válido")
    void shouldGetRoleByValidId() {
        Role admin = Role.fromId(1L);
        Role asesor = Role.fromId(2L);
        Role cliente = Role.fromId(3L);

        assertEquals(Role.ADMIN, admin);
        assertEquals(Role.ASESOR, asesor);
        assertEquals(Role.CLIENTE, cliente);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException para ID inválido")
    void shouldThrowIllegalArgumentExceptionForInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> Role.fromId(0L));
        assertThrows(IllegalArgumentException.class, () -> Role.fromId(4L));
        assertThrows(IllegalArgumentException.class, () -> Role.fromId(-1L));
        assertThrows(IllegalArgumentException.class, () -> Role.fromId(999L));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException para ID nulo")
    void shouldThrowIllegalArgumentExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> Role.fromId(null));
    }

    @Test
    @DisplayName("Debe verificar que todos los roles tienen IDs únicos")
    void shouldVerifyAllRolesHaveUniqueIds() {
        long adminId = Role.ADMIN.getId();
        long asesorId = Role.ASESOR.getId();
        long clienteId = Role.CLIENTE.getId();

        assertNotEquals(adminId, asesorId);
        assertNotEquals(adminId, clienteId);
        assertNotEquals(asesorId, clienteId);
    }

    @Test
    @DisplayName("Debe verificar que todos los roles tienen nombres únicos")
    void shouldVerifyAllRolesHaveUniqueNames() {
        String adminName = Role.ADMIN.getName();
        String asesorName = Role.ASESOR.getName();
        String clienteName = Role.CLIENTE.getName();

        assertNotEquals(adminName, asesorName);
        assertNotEquals(adminName, clienteName);
        assertNotEquals(asesorName, clienteName);
    }


    @Test
    @DisplayName("Debe verificar que los valores del enum son correctos")
    void shouldVerifyEnumValuesAreCorrect() {
        Role[] roles = Role.values();

        assertEquals(3, roles.length);
        assertTrue(java.util.Arrays.asList(roles).contains(Role.ADMIN));
        assertTrue(java.util.Arrays.asList(roles).contains(Role.ASESOR));
        assertTrue(java.util.Arrays.asList(roles).contains(Role.CLIENTE));
    }

}
