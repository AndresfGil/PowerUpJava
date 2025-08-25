package co.com.crediya.pragma.model.user.exception;

import java.util.List;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(String email) {
        super(
                "El correo ya está registrado: " + email,
                "EMAIL_DUPLICATE",
                "Email duplicado",
                409,
                List.of("El email " + email + " ya existe en el sistema")
        );
    }
}
