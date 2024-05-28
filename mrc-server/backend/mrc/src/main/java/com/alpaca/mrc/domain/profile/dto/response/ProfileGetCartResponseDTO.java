package com.alpaca.mrc.domain.profile.dto.response;


import lombok.Builder;
import java.util.List;

@Builder
public record ProfileGetCartResponseDTO(List<CartDTO> carts) {
    public static ProfileGetCartResponseDTO of(List<CartDTO> carts) {
        return ProfileGetCartResponseDTO.builder()
                .carts(carts)
                .build();
    }
}
