package com.alpaca.mrc.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // COMMON
    COMMON_ERROR(400, "CE001", "오류가 발생했습니다."),

    // SHOP
    SHOP_CART_NOT_FOUND(404, "SE001", "존재하지 않는 카트입니다."),
    SHOP_AVATAR_NOT_FOUND(404, "SE002", "존재하지 않는 아바타입니다."),
    SHOP_MY_CART_NOT_FOUND(404, "SE003", "존재하지 않는 마이 카트입니다."),
    SHOP_MY_AVATAR_NOT_FOUND(404, "SE004", "존재하지 않는 마이 아바타입니다."),


    // MEMBER
    MEMBER_NOT_FOUND(404, "ME001", "존재하지 않는 유저입니다."),
    MEMBER_DUFLICATED(409, "ME002", "중복된 유저입니다."),

    // ROOM
    ROOM_NOT_FOUND(404, "RE001", "방을 찾을 수 없습니다."),

    // RECORD


    // PROFILE

    SHOP_CART_DUPLICATED(409, "SE001", "중복된 카트입니다."),
    SHOP_AVATAR_DUPLICATED(409, "SE002", "중복된 아바타입니다."),
    SHOP_MY_AVATAR_DUPLICATED(409, "SE003", "이미 소유하고 있는 아바타입니다."),
    SHOP_MY_CART_DUPLICATED(409, "SE004", "이미 소유하고 있는 카트입니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
