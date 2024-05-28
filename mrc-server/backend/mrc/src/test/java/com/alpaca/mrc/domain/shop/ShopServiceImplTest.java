package com.alpaca.mrc.domain.shop;


import com.alpaca.mrc.domain.shop.Service.ShopServiceImpl;
import com.alpaca.mrc.domain.shop.dto.response.RandomAvatarResponseDTO;
import com.alpaca.mrc.domain.shop.dto.response.RandomCartResponseDTO;
import com.alpaca.mrc.domain.shop.entity.Avatar;
import com.alpaca.mrc.domain.shop.entity.Cart;
import com.alpaca.mrc.domain.shop.repository.AvatarRepository;
import com.alpaca.mrc.domain.shop.repository.CartRepository;
import com.alpaca.mrc.domain.shop.util.ItemGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ShopServiceImplTest {

    @Mock
    private AvatarRepository avatarRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private ShopServiceImpl shopService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRandomAvatarTest() {
        // 가상의 Avatar 리스트를 생성
        Avatar avatar1 = new Avatar(1L, "Avatar1", ItemGrade.COMMON);
        Avatar avatar2 = new Avatar(2L, "Avatar2", ItemGrade.COMMON);
        Avatar avatar3 = new Avatar(3L, "Avatar3", ItemGrade.COMMON);
        Avatar avatar4 = new Avatar(4L, "Avatar4", ItemGrade.COMMON);
        Avatar avatar5 = new Avatar(5L, "Avatar5", ItemGrade.RARE);
        Avatar avatar6 = new Avatar(6L, "Avatar6", ItemGrade.RARE);
        Avatar avatar7 = new Avatar(7L, "Avatar7", ItemGrade.UNIQUE);
        Avatar avatar8 = new Avatar(8L, "Avatar8", ItemGrade.UNIQUE);
        Avatar avatar9 = new Avatar(9L, "Avatar9", ItemGrade.LEGENDARY);
        List<Avatar> avatars = Arrays.asList(avatar1, avatar2, avatar3, avatar4, avatar5, avatar6, avatar7, avatar8, avatar9);

        // avatarRepository의 findAll 메서드가 avatars 리스트를 반환하도록 설정
        when(avatarRepository.findByGrade(ItemGrade.COMMON)).thenReturn(Arrays.asList(avatar1, avatar2, avatar3, avatar4));
        when(avatarRepository.findByGrade(ItemGrade.RARE)).thenReturn(Arrays.asList(avatar5, avatar6));
        when(avatarRepository.findByGrade(ItemGrade.UNIQUE)).thenReturn(Arrays.asList(avatar7, avatar8));
        when(avatarRepository.findByGrade(ItemGrade.LEGENDARY)).thenReturn(Collections.singletonList(avatar9));

        // 메서드 실행
        RandomAvatarResponseDTO result = shopService.getRandomAvatar();

        // 결과 디버깅
        System.out.println("Random Avatar: " + result.avatar().getName());

        // 결과 검증
        assertNotNull(result);
        assertTrue(avatars.stream().anyMatch(a -> a.getName().equals(result.avatar().getName())));
    }

    @Test
    void getRandomCartTest() {
        // 가상의 Cart 리스트를 생성
        Cart cart1 = new Cart(1L, "Cart1", ItemGrade.COMMON);
        Cart cart2 = new Cart(2L, "Cart2", ItemGrade.COMMON);
        Cart cart3 = new Cart(3L, "Cart3", ItemGrade.COMMON);
        Cart cart4 = new Cart(4L, "Cart4", ItemGrade.COMMON);
        Cart cart5 = new Cart(5L, "Cart5", ItemGrade.RARE);
        Cart cart6 = new Cart(6L, "Cart6", ItemGrade.RARE);
        Cart cart7 = new Cart(7L, "Cart7", ItemGrade.UNIQUE);
        Cart cart8 = new Cart(8L, "Cart8", ItemGrade.UNIQUE);
        Cart cart9 = new Cart(9L, "Cart9", ItemGrade.LEGENDARY);
        List<Cart> carts = Arrays.asList(cart1, cart2, cart3, cart4, cart5, cart6, cart7, cart8);

        // cartRepository의 findAll 메서드가 carts 리스트를 반환하도록 설정
        when(cartRepository.findAll()).thenReturn(carts);

        // 메서드 실행
        RandomCartResponseDTO result = shopService.getRandomCart();

        // 결과 디버깅
        System.out.println("Random Cart: " + result.cart().getName());

        // 결과 검증
        assertNotNull(result);
        assertTrue(carts.stream().anyMatch(a -> a.getName().equals(result.cart().getName())));
    }
}
