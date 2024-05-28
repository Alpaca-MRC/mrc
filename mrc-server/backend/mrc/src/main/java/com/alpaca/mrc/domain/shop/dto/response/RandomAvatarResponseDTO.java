package com.alpaca.mrc.domain.shop.dto.response;

import com.alpaca.mrc.domain.shop.entity.Avatar;
import lombok.Builder;

@Builder
public record RandomAvatarResponseDTO(Avatar avatar) {

    public static RandomAvatarResponseDTO of(Avatar avatar) {
        return RandomAvatarResponseDTO.builder()
                .avatar(avatar)
                .build();
    }
}
