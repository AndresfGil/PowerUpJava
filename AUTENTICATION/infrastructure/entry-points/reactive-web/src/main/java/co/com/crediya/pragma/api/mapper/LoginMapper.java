package co.com.crediya.pragma.api.mapper;

import co.com.crediya.pragma.api.dto.LoginResponseDTO;
import co.com.crediya.pragma.model.user.Role;
import co.com.crediya.pragma.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public LoginResponseDTO toResponse(User user, String token) {
        Role role = Role.fromId(user.getRolId());

        return LoginResponseDTO.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .lastname(user.getLastname())
                .rolId(user.getRolId())
                .roleName(role.getName())
                .build();
    }
}
