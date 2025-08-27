package co.com.crediya.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastname;

    @Schema(description = "Correo electrónico", example = "juan.perez@dominio.com")
    private String email;

    @Schema(description = "Salario base del usuario", example = "15000000")
    private BigDecimal baseSalary;

    @Schema(description = "Teléfono de contacto", example = "3214567890")
    private String phone;

    @Schema(description = "Dirección del usuario", example = "Calle 123 #45-67")
    private String address;
}
