package com.alpaca.mrc.domain.room.dto.response;

public record RoomCreateResponseDTO(String roomId) {
    public static RoomCreateResponseDTO of(String roomId) {
        return new RoomCreateResponseDTO(roomId);
    }
}
