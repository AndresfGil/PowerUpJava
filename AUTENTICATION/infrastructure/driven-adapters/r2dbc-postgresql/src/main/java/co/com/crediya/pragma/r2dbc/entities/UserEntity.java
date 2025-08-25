package co.com.crediya.pragma.r2dbc.entities;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.math.BigDecimal;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @Column("user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String lastname;
    private String email;
    @Column("document_id")
    private String documentId;
    private String address;
    @Column("birth_date")
    private String birthDate;
    private Double phone;
    @Column("base_salary")
    private Double baseSalary;


    @Column("id_rol")
    private Long idRol;


}
