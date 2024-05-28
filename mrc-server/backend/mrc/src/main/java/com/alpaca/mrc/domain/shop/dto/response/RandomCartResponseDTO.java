package com.alpaca.mrc.domain.shop.dto.response;

import com.alpaca.mrc.domain.shop.entity.Cart;
import lombok.Builder;

@Builder
public record RandomCartResponseDTO(Cart cart) {

    public static RandomCartResponseDTO of(Cart cart) {
        return RandomCartResponseDTO.builder()
                .cart(cart)
                .build();
    }
}
