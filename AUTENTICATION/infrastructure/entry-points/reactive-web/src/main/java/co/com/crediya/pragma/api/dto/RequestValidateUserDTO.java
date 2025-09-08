package co.com.crediya.pragma.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RequestValidateUserDTO(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String documentoIdentidad

) {
}
