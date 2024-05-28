package com.alpaca.mrc.domain.profile.controller;

import com.alpaca.mrc.domain.profile.dto.request.ProfileUpdateAvatarRequestDTO;
import com.alpaca.mrc.domain.profile.dto.request.ProfileUpdateCartRequsetDTO;
import com.alpaca.mrc.domain.profile.service.ProfileServiceImpl;
import com.alpaca.mrc.global.result.ResultCode;
import com.alpaca.mrc.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@Slf4j
public class ProfileController {

    private final ProfileServiceImpl profileService;

    @Operation(summary = "카트 조회")
    @GetMapping("/cart")
    public ResponseEntity<ResultResponse> getCart() {

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PROFILE_GET_CART_SUCCESS, profileService.getCart()));
    }

    @Operation(summary = "카트 변경")
    @PostMapping("/cart")
    public ResponseEntity<ResultResponse> changeCart(@RequestBody ProfileUpdateCartRequsetDTO requestDTO) {
        profileService.changeCart(requestDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PROFILE_CHANGE_CART_SUCCESS));
    }

    @Operation(summary = "아바타 조회")
    @GetMapping("/avatar")
    public ResponseEntity<ResultResponse> getAvatar() {

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PROFILE_GET_AVATAR_SUCCESS, profileService.getAvatar()));
    }

    @Operation(summary = "아바타 변경")
    @PostMapping("/avatar")
    public ResponseEntity<ResultResponse> changeAvatar(@RequestBody ProfileUpdateAvatarRequestDTO requestDTO) {

        profileService.changeAvatar(requestDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PROFILE_CHANGE_AVATAR_SUCCESS));
    }


}
