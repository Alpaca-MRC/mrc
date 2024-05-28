package com.alpaca.mrc.domain.shop.Service;

import com.alpaca.mrc.domain.shop.dto.request.CreateAvatarRequestDTO;
import com.alpaca.mrc.domain.shop.dto.request.CreateCartRequestDTO;
import com.alpaca.mrc.domain.shop.dto.response.RandomAvatarResponseDTO;
import com.alpaca.mrc.domain.shop.dto.response.RandomCartResponseDTO;

public interface ShopService {
    // 랜덤 아바타 뽑기
    RandomAvatarResponseDTO getRandomAvatar();
    // 랜덤 카트 뽑기
    RandomCartResponseDTO getRandomCart();
    // 아바타 추가
    void createAvatar(CreateAvatarRequestDTO requestDTO);
    // 카트 추가
    void createCart(CreateCartRequestDTO requestDTO);
}
