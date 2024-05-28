package com.alpaca.mrc.domain.profile.service;

import com.alpaca.mrc.domain.profile.dto.request.ProfileUpdateAvatarRequestDTO;
import com.alpaca.mrc.domain.profile.dto.request.ProfileUpdateCartRequsetDTO;
import com.alpaca.mrc.domain.profile.dto.response.ProfileGetAvatarResponseDTO;
import com.alpaca.mrc.domain.profile.dto.response.ProfileGetCartResponseDTO;

public interface ProfileService {

    // 카트 조회
    ProfileGetCartResponseDTO getCart();
    // 카트 변경
    void changeCart(ProfileUpdateCartRequsetDTO requestDTO);
    // 아바타 조회
    ProfileGetAvatarResponseDTO getAvatar();
    // 아바타 변경
    void changeAvatar(ProfileUpdateAvatarRequestDTO requestDTO);
}
