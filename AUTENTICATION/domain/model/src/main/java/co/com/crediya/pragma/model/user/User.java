package co.com.crediya.pragma.model.user;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long userId;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String documentId;
    private String address;
    private String birthDate;
    private String phone;
    private Long rolId;
    private BigDecimal baseSalary;

    public User(Long userId, String email, String password, String name, String lastname, Long rolId, String documentId, BigDecimal baseSalary, String address, String phone, String birthDate) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.rolId = rolId;
        this.documentId = documentId;
        this.baseSalary = baseSalary;
        this.address = address;
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
