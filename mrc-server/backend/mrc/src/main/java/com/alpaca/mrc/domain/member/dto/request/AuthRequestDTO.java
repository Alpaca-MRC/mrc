package com.alpaca.mrc.domain.member.dto.request;

import com.alpaca.mrc.domain.member.entity.AccountType;
import lombok.Getter;

@Getter
public class AuthRequestDTO {

    private String username;
    private String nickname;
    private AccountType accountType;
}
