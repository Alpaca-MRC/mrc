package com.alpaca.mrc.domain.room.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 방 생성
    public void createRoom(String roomId, String username, int maxPlayerNumber) {

        // 키 생성
        String roomKey = "room_" + roomId;

        // 값 생성
        List<String> playerList = new ArrayList<>();
        playerList.add(username);
        Map<String, Object> roomInfo = Map.of(
                "host", username,
                "playerList", playerList,
                "maxPlayerNumber", maxPlayerNumber
        );

        // 저장
        redisTemplate.opsForSet().add(roomKey, roomInfo);
    }

    // 방 입장
    public void enterRoom(String roomId, String username) {

        // 키 생성
        String roomKey = "room_" + roomId;

        // 방 조회
        Map<String, Object> roomInfo = (Map<String, Object>) redisTemplate.opsForValue().get(roomKey);

        // 방에 추가
        List<String> playerList = (List<String>) roomInfo.get("playerList");
        playerList.add(username);
        roomInfo.replace("playerList", playerList);



        redisTemplate.opsForSet().add(roomKey, username);
    }

    // 방 퇴장
    public void leaveRoom(String roomId, String username) {
        String roomKey = "room_" + roomId;
        redisTemplate.opsForSet().remove(roomKey, username);
    }

    // 방 조회
    public Map<Object, Object> getRoomInfo(String roomId) {
        String roomKey = "room_" + roomId;
        return redisTemplate.opsForHash().entries(roomKey);
    }
}
