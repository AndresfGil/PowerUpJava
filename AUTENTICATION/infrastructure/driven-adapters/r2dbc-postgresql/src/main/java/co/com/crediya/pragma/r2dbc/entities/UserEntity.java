package co.com.crediya.pragma.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class UserEntity {

    @Id
    @Column("user_id")
    private Long userId;

    private String name;
    private String lastname;
    private String email;

    @Column("document_id")
    private String documentId;

    private String address;

    @Column("birth_date")
    private String birthDate;

    private String phone;

    @Column("base_salary")
    private Double baseSalary;

    @Column("rol_id")
    private Long rolId;

}
