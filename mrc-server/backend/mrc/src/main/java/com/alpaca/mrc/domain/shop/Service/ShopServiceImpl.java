package com.alpaca.mrc.domain.shop.Service;

import com.alpaca.mrc.domain.member.entity.Member;
import com.alpaca.mrc.domain.member.exception.MemberException;
import com.alpaca.mrc.domain.member.repository.MemberRepository;
import com.alpaca.mrc.domain.shop.dto.request.CreateAvatarRequestDTO;
import com.alpaca.mrc.domain.shop.dto.request.CreateCartRequestDTO;
import com.alpaca.mrc.domain.shop.dto.response.RandomAvatarResponseDTO;
import com.alpaca.mrc.domain.shop.dto.response.RandomCartResponseDTO;
import com.alpaca.mrc.domain.shop.entity.Avatar;
import com.alpaca.mrc.domain.shop.entity.Cart;
import com.alpaca.mrc.domain.shop.entity.MyAvatar;
import com.alpaca.mrc.domain.shop.entity.MyCart;
import com.alpaca.mrc.domain.shop.exception.ShopException;
import com.alpaca.mrc.domain.shop.repository.AvatarRepository;
import com.alpaca.mrc.domain.shop.repository.CartRepository;
import com.alpaca.mrc.domain.shop.repository.MyAvatarRepository;
import com.alpaca.mrc.domain.shop.repository.MyCartRepository;
import com.alpaca.mrc.domain.shop.util.ItemGrade;
import com.alpaca.mrc.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final AvatarRepository avatarRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final MyAvatarRepository myAvatarRepository;
    private final MyCartRepository myCartRepository;
    private static final SecureRandom random = new SecureRandom();

    // 아바타 추가
    @Transactional
    public void createAvatar(CreateAvatarRequestDTO requestDTO) {
        Avatar avatar = Avatar.builder()
                .name(requestDTO.getName())
                .grade(requestDTO.getGrade())
                .build();

        // 중복 체크
        if (avatarRepository.existsByName(avatar.getName())) {
            throw new ShopException(ErrorCode.SHOP_AVATAR_DUPLICATED);
        }
        avatarRepository.save(avatar);
    }

    // 카트 추가
    @Transactional
    public void createCart(CreateCartRequestDTO requestDTO) {
        Cart cart = Cart.builder()
                .name(requestDTO.getName())
                .grade(requestDTO.getGrade())
                .build();

        // 중복체크
        if (cartRepository.existsByName(cart.getName())) {
            throw new ShopException(ErrorCode.SHOP_CART_DUPLICATED);
        }
        cartRepository.save(cart);
    }

    // 아바타 뽑기
    @Transactional
    public RandomAvatarResponseDTO getRandomAvatar() {

        // 유저 조회
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 등급 픽
        ItemGrade itemGrade = getItemGrade();

        // 아바타 픽
        Avatar avatar = getRandomAvatar(itemGrade);


        // 중개테이블 생성
        MyAvatar myAvatar = MyAvatar.builder()
                .avatar(avatar)
                .member(member)
                .isSelected(false)
                .build();

        // 소유하고 있는 아바타인지 체크
        if (!myAvatarRepository.existsByMemberAndAvatar(member, avatar)) {

            myAvatarRepository.save(myAvatar);
            myAvatar.setMember(member);
        }

        return RandomAvatarResponseDTO.of(avatar);
    }

    // 카트 뽑기
    @Transactional
    public RandomCartResponseDTO getRandomCart() {

        // 유저 조회
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 등급 픽
        ItemGrade itemGrade = getItemGrade();

        // 카트 픽
        Cart cart = getRandomCart(itemGrade);


        // 중개테이블 생성
        MyCart myCart = MyCart.builder()
                .cart(cart)
                .member(member)
                .isSelected(false)
                .build();

        // 소유하고 있는 카트인지 체크
        if (!myCartRepository.existsByMemberAndCart(member, cart)) {
            myCartRepository.save(myCart);
            myCart.setMember(member);
        }

        return RandomCartResponseDTO.of(cart);
    }

    // 등급 반환 함수
    private ItemGrade getItemGrade() {
        int randomValue = random.nextInt(101); // 0 이상 100 이하의 랜덤한 정수 생성
        int[] probabilities = {70, 24, 5, 1};
        int total = 0;

        for (int i = 0 ; i < 4; i++) {
            total += probabilities[i];
            if (randomValue <= total) return ItemGrade.fromGradeNum(i);

        }
        return ItemGrade.COMMON;
    }

    // 등급 내 아이템 무작위 반환
    private Cart getRandomCart(ItemGrade itemGrade) {
        // 전체 아이템을 돌면서 해당 등급의 아이템들을 가져온 후
        List<Cart> carts = cartRepository.findByGrade(itemGrade);
        // 아이템 배열 shuffle
        Collections.shuffle(carts);
        // 이후 맨 앞에 있는 것을 가져온다
        return carts.getFirst();
    }

    private Avatar getRandomAvatar(ItemGrade itemGrade) {
        // 해당 등급 아바타 조회
        List<Avatar> avatars = avatarRepository.findByGrade(itemGrade);
        // 아바타 배열 shuffle
        Collections.shuffle(avatars);
        // 이후 맨 앞에 있는 것을 가져온다
        return avatars.getFirst();
    }
}
