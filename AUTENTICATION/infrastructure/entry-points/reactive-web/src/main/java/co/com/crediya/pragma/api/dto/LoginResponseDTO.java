package co.com.crediya.pragma.api.dto;


public record LoginResponseDTO(
        String accessToken,
        String tokenType,
        Long expiresInMinutes
) {}
