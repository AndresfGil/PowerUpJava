package co.com.crediya.pragma.r2dbc.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleEntityTest {

    @Test
    @DisplayName("Debe crear RoleEntity con todos los campos correctamente")
    void shouldCreateRoleEntityWithAllFieldsCorrectly() {
        RoleEntity roleEntity = RoleEntity.builder()
                .rolId(1L)
                .name("Administrador")
                .description("Rol con permisos de administración completa")
                .build();

        assertEquals(1L, roleEntity.getRolId());
        assertEquals("Administrador", roleEntity.getName());
        assertEquals("Rol con permisos de administración completa", roleEntity.getDescription());
    }

}
