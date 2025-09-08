package co.com.crediya.pragma.model.user.exception;

import java.util.List;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(
                message,
                "UNAUTHORIZED",
                "No autorizado",
                403,
                List.of(message)
        );
    }

}
