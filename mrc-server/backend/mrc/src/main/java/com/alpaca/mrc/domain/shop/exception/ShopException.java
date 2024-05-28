package com.alpaca.mrc.domain.shop.exception;

import com.alpaca.mrc.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class ShopException extends RuntimeException{

    private final ErrorCode errorCode;
    public ShopException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
