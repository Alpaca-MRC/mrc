package com.alpaca.mrc.domain.member.dto.response;

public record AuthResponseDTO(String accessToken, String refreshToken, Long expiresIn) {

    public static AuthResponseDTO of(String accessToken, String refreshToken, Long expiresIn) {
        return new AuthResponseDTO(accessToken, refreshToken, expiresIn);
    }
}
