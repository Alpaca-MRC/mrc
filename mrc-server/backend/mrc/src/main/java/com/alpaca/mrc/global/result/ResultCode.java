package com.alpaca.mrc.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // COMMON
    SUCCESS(200, "C001", "정상 처리 되었습니다"),

    // SHOP
    SHOP_GET_RANDOM_CART_SUCCESS(200, "S001", "카트 랜덤 뽑기를 성공했습니다."),
    SHOP_GET_RANDOM_AVATAR_SUCCESS(200, "S002", "아바타 랜덤 뽑기를 성공했습니다."),
    SHOP_CREATE_CART_SUCCESS(200, "S003", "카트 생성에 성공했습니다."),
    SHOP_CREATE_AVATAR_SUCCESS(200, "S004", "아바타 생성에 성공했습니다."),

    // MEMBER
    MEMBER_AUTHENTICATION_SUCCES(200, "M001", "회원 인증을 성공했습니다."),

    // RECORD
    RECORD_GET_INFO_SUCCESS(200, "R001", "전적 조회를 성공했습니다."),

    // PROFILE
    PROFILE_GET_CART_SUCCESS(200, "P001", "카트 조회를 성공했습니다."),
    PROFILE_GET_AVATAR_SUCCESS(200, "P002", "아바타 조회를 성공했습니다."),
    PROFILE_CHANGE_CART_SUCCESS(200, "P003", "카트 변경을 성공했습니다."),
    PROFILE_CHANGE_AVATAR_SUCCESS(200, "P004", "아바타 변경을 성공했습니다."),

    // ROOM
    ROOM_CREATE_SUCCESS(200, "R001", "방 생성을 성공했습니다."),
    ROOM_ENTER_SUCCESS(200, "R002", "방 입장에 성공했습니다."),
    ROOM_LEAVE_SUCCESS(200, "R003", "방 나가기를 성공했습니다."),
    ROOM_GET_INFO_SUCCESS(200, "R004", "방 조회에 성공했습니다.")
    ;


    private final int status;
    private final String code;
    private final String message;
}
