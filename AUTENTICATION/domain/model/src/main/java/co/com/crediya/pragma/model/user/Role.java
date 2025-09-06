package co.com.crediya.pragma.model.user;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {
    private Long rolId;
    private String name;
    private String description;
}
