package co.com.crediya.pragma.model.user.exception;

import java.util.List;

public class RolNotFoundException extends BaseException {
    
    public RolNotFoundException() {
        super(
                "Rol no encontrado",
                "ROLE_NOT_FOUND",
                "Rol no identificado",
                404,
                List.of("El rol especificado no existe")
        );
    }
    
    public RolNotFoundException(String roleId) {
        super(
                "Rol no encontrado: " + roleId,
                "ROLE_NOT_FOUND",
                "Rol no encontrado",
                404,
                List.of("El rol con ID " + roleId + " no existe")
        );
    }
}
