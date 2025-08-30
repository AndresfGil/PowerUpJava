package co.com.crediya.pragma.api.dto;

import lombok.Builder;

@Builder
public record LoginResponseDTO(
        String token,
        String email,
        String name,
        String lastname,
        Long rolId,
        String roleName
) {}
