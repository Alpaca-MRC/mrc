package com.alpaca.mrc.domain.room.dto.response;

import com.alpaca.mrc.domain.room.model.Room;

public record RoomInfoResponseDTO(Room room) {

    public static RoomInfoResponseDTO of(Room room) {
        return new RoomInfoResponseDTO(room);
    }
}
