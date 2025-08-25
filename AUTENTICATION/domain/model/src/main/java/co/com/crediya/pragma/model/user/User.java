package co.com.crediya.pragma.model.user;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long idNumber;
    private String name;
    private String lastname;
    private String email;
    private String documentId;
    private String address;
    private String birthDate;
    private String phone;
    private BigDecimal idRol;
    private BigDecimal baseSalary;
}
