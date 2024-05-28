package com.alpaca.mrc.domain.room.repository;

import com.alpaca.mrc.domain.room.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@EnableRedisRepositories
public class RoomRepository {

    private static final String ROOM_HASH_KEY = "rooms";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Room> hashOperations;

    @Autowired
    public RoomRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    // 방 저장
    public void saveRoomData(String roomId, Room room) {
        hashOperations.put(ROOM_HASH_KEY, roomId, room);
    }

    // 방 조회
    public Room getRoomData(String roomId) {
        return hashOperations.get(ROOM_HASH_KEY, roomId);
    }

    // 방 수정
    public void updateRoomData(String roomId, Room updatedRoom) {
        hashOperations.put(ROOM_HASH_KEY, roomId, updatedRoom);
    }

    // 방 삭제
    public void deleteRoomData(String roomId) {
        hashOperations.delete(ROOM_HASH_KEY, roomId);
    }

    // 방장 위임
    public void delegateHost(String roomId, Room room) {
        List<String> playerList = room.getPlayerList();
        int index = (int) (Math.random() * playerList.size());
        String newHost = playerList.get(index);
        room.setHost(newHost);
        updateRoomData(roomId, room);
    }
}
