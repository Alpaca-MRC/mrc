package com.alpaca.mrc.domain.member.exception;

import com.alpaca.mrc.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

    private final ErrorCode errorCode;
    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}