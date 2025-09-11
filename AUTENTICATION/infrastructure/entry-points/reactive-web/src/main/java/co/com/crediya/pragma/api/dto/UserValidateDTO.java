package co.com.crediya.pragma.api.dto;

import java.math.BigDecimal;

public record UserValidateDTO(boolean exists, BigDecimal baseSalary) {
}
