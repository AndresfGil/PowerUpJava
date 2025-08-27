package co.com.crediya.pragma.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class RoleEntity {

    @Id
    @Column("rol_id")
    private Long rolId;

    private String name;

    private String description;

}
