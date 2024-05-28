package com.alpaca.mrc.domain.shop.controller;

import com.alpaca.mrc.domain.shop.Service.ShopServiceImpl;
import com.alpaca.mrc.domain.shop.dto.request.CreateAvatarRequestDTO;
import com.alpaca.mrc.domain.shop.dto.request.CreateCartRequestDTO;
import com.alpaca.mrc.global.result.ResultCode;
import com.alpaca.mrc.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/shop")
@Slf4j
public class ShopController {

    private final ShopServiceImpl shopService;

    // 아바타 추가
    @Operation(summary = "아바타 추가")
    @PostMapping("/avatar/create")
    public ResponseEntity<ResultResponse> createAvatar(@RequestBody CreateAvatarRequestDTO requestDTO) {
        shopService.createAvatar(requestDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.SHOP_CREATE_AVATAR_SUCCESS));
    }

    // 카트 추가
    @Operation(summary = "카트 추가")
    @PostMapping("/cart/create")
    public ResponseEntity<ResultResponse> createCart(@RequestBody CreateCartRequestDTO requestDTO) {
        shopService.createCart(requestDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.SHOP_CREATE_CART_SUCCESS));
    }

    // 아바타 뽑기
    @Operation(summary = "아바타 뽑기")
    @PostMapping("/avatar")
    public ResponseEntity<ResultResponse> getRandomAvatar() {

        return ResponseEntity.ok(ResultResponse.of(ResultCode.SHOP_GET_RANDOM_AVATAR_SUCCESS, shopService.getRandomAvatar()));
    }

    // 카트 뽑기
    @Operation(summary = "카트 뽑기")
    @PostMapping("/cart")
    public ResponseEntity<ResultResponse> getRandomCart() {

        return ResponseEntity.ok(ResultResponse.of(ResultCode.SHOP_GET_RANDOM_CART_SUCCESS, shopService.getRandomCart()));
    }
}
