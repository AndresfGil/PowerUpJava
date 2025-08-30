package co.com.crediya.pragma.api.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SaveUserDTO {
    @JsonIgnore
    private Long userId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 120, message = "El apellido no puede exceder 120 caracteres")
    private String lastname;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 180, message = "El email no puede exceder 180 caracteres")
    private String email;


    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "1", message = "El salario debe ser mayor o igual a 0")
    @DecimalMax(value = "15000000", message = "El salario no puede exceder 15,000,000")
    private BigDecimal baseSalary;

    @Size(max = 12, message = "El documento no puede exceder 30 caracteres")
    private String documentId;

    @Size(max = 12, message = "El teléfono no puede exceder 30 caracteres")
    private String phone;

    @Size(max = 100, message = "La dirección no puede exceder 200 caracteres")
    private String address;

    private String birthDate;

    private Long rolId;
}
