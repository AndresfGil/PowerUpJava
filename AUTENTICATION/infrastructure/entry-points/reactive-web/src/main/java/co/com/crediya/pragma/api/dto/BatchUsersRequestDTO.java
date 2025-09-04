package co.com.crediya.pragma.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;


public record BatchUsersRequestDTO(
    @NotEmpty(message = "La lista de emails no puede estar vacía")
    @Size(max = 100, message = "No se pueden procesar más de 100 emails a la vez")
    List<String> emails
) {
}

