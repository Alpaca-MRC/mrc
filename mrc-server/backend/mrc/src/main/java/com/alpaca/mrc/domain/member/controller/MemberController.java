package com.alpaca.mrc.domain.member.controller;

import com.alpaca.mrc.domain.member.dto.request.AuthRequestDTO;
import com.alpaca.mrc.domain.member.service.MemberServiceImpl;
import com.alpaca.mrc.global.result.ResultCode;
import com.alpaca.mrc.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;

    // 회원 인증
    @Operation(summary = "회원 인증")
    @PostMapping("/auth")
    public ResponseEntity<ResultResponse> authentication(@RequestBody AuthRequestDTO requestDto) {

        return ResponseEntity.ok(ResultResponse.of(ResultCode.MEMBER_AUTHENTICATION_SUCCES, memberService.authentication(requestDto)));
    }

}
