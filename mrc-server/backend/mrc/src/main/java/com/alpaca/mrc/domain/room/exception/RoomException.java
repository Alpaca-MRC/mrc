package com.alpaca.mrc.domain.room.exception;

import com.alpaca.mrc.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class RoomException extends RuntimeException {

    private final ErrorCode errorCode;
    public RoomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
