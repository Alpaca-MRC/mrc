package com.alpaca.mrc.domain.room.service;

import com.alpaca.mrc.domain.room.dto.response.RoomCreateResponseDTO;
import com.alpaca.mrc.domain.room.dto.response.RoomInfoResponseDTO;

public interface RoomService {

    // 방 생성
    RoomCreateResponseDTO createRoom(int maxPlayerNumber);

    // 방 입장
    void enterRoom(String roomId);

    // 방 퇴장
    void leaveRoom(String roomId);

    // 방 정보, 유저 목록 조회
    RoomInfoResponseDTO getRoomInfo(String roomId);
}
