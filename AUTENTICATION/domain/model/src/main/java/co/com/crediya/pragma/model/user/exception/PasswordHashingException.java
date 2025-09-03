package co.com.crediya.pragma.model.user.exception;

import java.util.List;

public class PasswordHashingException extends BaseException {
    public PasswordHashingException(String message, Throwable cause) {
        super(
                message,
                "PASSWORD_HASHING_ERROR",
                "Error al procesar contraseña",
                500,
                List.of("Error interno al procesar la contraseña del usuario")
        );
    }
}
