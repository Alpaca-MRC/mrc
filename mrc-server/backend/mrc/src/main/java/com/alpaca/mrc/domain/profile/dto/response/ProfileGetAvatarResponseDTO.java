package com.alpaca.mrc.domain.profile.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfileGetAvatarResponseDTO(List<AvatarDTO> avatars) {

    public static ProfileGetAvatarResponseDTO of(List<AvatarDTO> avatars) {
        return ProfileGetAvatarResponseDTO.builder()
                .avatars(avatars)
                .build();
    }
}
