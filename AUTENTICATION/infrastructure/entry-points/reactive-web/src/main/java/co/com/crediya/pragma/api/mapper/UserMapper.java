package co.com.crediya.pragma.api.mapper;

import co.com.crediya.pragma.api.dto.SaveUserDTO;
import co.com.crediya.pragma.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(SaveUserDTO dto) {
        return User.builder()
                .userId(dto.getUserId())
                .name(trim(dto.getName()))
                .lastname(trim(dto.getLastname()))
                .email(lower(trim(dto.getEmail())))
                .documentId(trim(dto.getDocumentId()))
                .phone(trim(dto.getPhone()))
                .address(trim(dto.getAddress()))
                .birthDate(dto.getBirthDate())
                .rolId(dto.getRolId())
                .baseSalary(dto.getBaseSalary())
                .build();
    }

    private String trim(String s) { return s == null ? null : s.trim(); }
    private String lower(String s) { return s == null ? null : s.toLowerCase(); }
}
