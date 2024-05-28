package com.alpaca.mrc.domain.member.service;

import com.alpaca.mrc.domain.member.dto.request.AuthRequestDTO;
import com.alpaca.mrc.domain.member.dto.response.AuthResponseDTO;
import com.alpaca.mrc.domain.member.entity.Member;
import com.alpaca.mrc.domain.member.exception.MemberException;
import com.alpaca.mrc.domain.member.repository.MemberRepository;
import com.alpaca.mrc.global.error.ErrorCode;
import com.alpaca.mrc.global.result.ResultCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    // 회원 인증
    @Transactional
    public AuthResponseDTO authentication(AuthRequestDTO requestDto) {

        // 회원 이미 있는 경우
        if (memberRepository.existsByUsername(requestDto.getUsername()))
        {

            System.out.println("로그인!!!!!");

            // username으로 회원 조회

            // 유저 일치 검증

        }
        // 회원이 없는 경우
        else
        {
            System.out.println("회원가입!!!!!");

            // 회원가입 처리
            Member member = Member.builder()
                    .username(requestDto.getUsername())
                    .nickname(requestDto.getNickname())
                    .accountType(requestDto.getAccountType())
                    .build();

            // db 저장
            memberRepository.save(member);
        }

        // 토큰 발급
        String accessToken = "asdf";
        String refreshToken = "asdf";
        Long expiresIn = 3600L;

        return AuthResponseDTO.of(accessToken, refreshToken, expiresIn);
    }
}
