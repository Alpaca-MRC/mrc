package com.alpaca.mrc.domain.profile.service;

import com.alpaca.mrc.domain.member.entity.Member;
import com.alpaca.mrc.domain.member.exception.MemberException;
import com.alpaca.mrc.domain.member.repository.MemberRepository;
import com.alpaca.mrc.domain.profile.dto.request.ProfileUpdateAvatarRequestDTO;
import com.alpaca.mrc.domain.profile.dto.request.ProfileUpdateCartRequsetDTO;
import com.alpaca.mrc.domain.profile.dto.response.*;
import com.alpaca.mrc.domain.shop.entity.Avatar;
import com.alpaca.mrc.domain.shop.entity.Cart;
import com.alpaca.mrc.domain.shop.entity.MyAvatar;
import com.alpaca.mrc.domain.shop.entity.MyCart;
import com.alpaca.mrc.domain.shop.exception.ShopException;
import com.alpaca.mrc.domain.shop.repository.AvatarRepository;
import com.alpaca.mrc.domain.shop.repository.CartRepository;
import com.alpaca.mrc.domain.shop.repository.MyAvatarRepository;
import com.alpaca.mrc.domain.shop.repository.MyCartRepository;
import com.alpaca.mrc.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final AvatarRepository avatarRepository;
    private final MyCartRepository myCartRepository;
    private final MyAvatarRepository myAvatarRepository;

    // 카트 조회
    public ProfileGetCartResponseDTO getCart() {

        // 유저 조회
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 유저의 모든 카트 조회
        List<MyCart> myCarts = member.getMyCarts();

        // 순회 돌면서 카트를 추가
        List<CartDTO> carts = new ArrayList<>();
        for (MyCart myCart: myCarts) {
            Cart cart = myCart.getCart();
            carts.add(CartDTO.builder()
                            .name(cart.getName())
                            .grade(cart.getGrade())
                            .build());
        }
        return ProfileGetCartResponseDTO.of(carts);
    }

    // 카트 변경
    @Transactional
    public void changeCart(ProfileUpdateCartRequsetDTO requestDTO) {

        // 유저 조회
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 유저의 이전 카트 조회
        String preCartName = member.getSelectedCartName();
        Optional<Cart> optionalCart = cartRepository.findByName(preCartName);

        // 이전 카트 비활성화
        if (optionalCart.isPresent()) {
            MyCart preMyCart = myCartRepository.findByMemberAndCart(member, optionalCart.get())
                    .orElseThrow(() -> new ShopException(ErrorCode.SHOP_MY_CART_NOT_FOUND));

            preMyCart.updateIsSelected();
        }

        // 변경할 카트 조회
        String selectedCartName = requestDTO.getName();
        System.out.println("selectedCartName = " + selectedCartName);
        Cart selectedCart = cartRepository.findByName(selectedCartName)
                .orElseThrow(() -> new ShopException(ErrorCode.SHOP_CART_NOT_FOUND));

        // 변경할 카트 조회
        MyCart curMyCart = myCartRepository.findByMemberAndCart(member, selectedCart)
                .orElseThrow(() -> new ShopException(ErrorCode.SHOP_MY_CART_NOT_FOUND));

        curMyCart.updateIsSelected();

        // 유저의 선택 카트 변경
        member.updateSelectedCartName(selectedCartName);
    }

    // 아바타 조회
    public ProfileGetAvatarResponseDTO getAvatar() {

        // 유저 조회
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 유저의 모든 아바타 조회
        List<MyAvatar> myAvatars = member.getMyAvatars();

        // 유저의 모든 아바타로부터 아바타 리스트 생성
        List<AvatarDTO> avatars = new ArrayList<>();
        for (MyAvatar myAvatar : myAvatars) {
            Avatar avatar = myAvatar.getAvatar();
            avatars.add(AvatarDTO.builder()
                            .name(avatar.getName())
                            .grade(avatar.getGrade())
                            .build());
        }

        return ProfileGetAvatarResponseDTO.of(avatars);
    }


    // 아바타 변경
    @Transactional
    public void changeAvatar(ProfileUpdateAvatarRequestDTO requestDTO) {

        // 유저 조회
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 유저의 이전 아바타 조회
        String preAvatarName = member.getSelectedAvatarName();
        Optional<Avatar> optionalAvatar = avatarRepository.findByName(preAvatarName);

        if (optionalAvatar.isPresent()) {
            // 이전 아바타 비활성화
            MyAvatar preMyAvatar = myAvatarRepository.findByMemberAndAvatar(member, optionalAvatar.get())
                    .orElseThrow(() -> new ShopException(ErrorCode.SHOP_MY_AVATAR_NOT_FOUND));

            preMyAvatar.updateIsSelected();
        }

        // 변경할 아바타 조회
        String selectedAvatarName = requestDTO.getName();
        Avatar selectedAvatar = avatarRepository.findByName(selectedAvatarName)
                .orElseThrow(() -> new ShopException(ErrorCode.SHOP_AVATAR_NOT_FOUND));

        // 변경할 아바타
        MyAvatar curMyAvatar = myAvatarRepository.findByMemberAndAvatar(member, selectedAvatar)
                .orElseThrow(() -> new ShopException(ErrorCode.SHOP_MY_AVATAR_NOT_FOUND));

        curMyAvatar.updateIsSelected();

        // 유저의 선택 아바타 변경
        member.updateSelectedAvatarName(selectedAvatarName);
    }
}
