package com.alpaca.mrc.domain.room.service;

import com.alpaca.mrc.domain.member.entity.Member;
import com.alpaca.mrc.domain.member.exception.MemberException;
import com.alpaca.mrc.domain.member.repository.MemberRepository;
import com.alpaca.mrc.domain.room.dto.response.RoomCreateResponseDTO;
import com.alpaca.mrc.domain.room.dto.response.RoomInfoResponseDTO;
import com.alpaca.mrc.domain.room.model.Room;
import com.alpaca.mrc.domain.room.repository.RoomRepository;
import com.alpaca.mrc.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    // 방 생성
    public RoomCreateResponseDTO createRoom(int maxPlayerNumber) {

        // 유저 조회
        Member member = memberRepository.findByUsername("hyungi")
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // roomId 생성
        String roomId = UUID.randomUUID().toString();

        // 방 객체 생성
        ArrayList<String> playerList = new ArrayList<>();
        playerList.add(member.getUsername());

        Room room = new Room(member.getUsername(), playerList, maxPlayerNumber);

        // 방 생성
        roomRepository.saveRoomData(roomId, room);

        return RoomCreateResponseDTO.of(roomId);
    }

    // 방 입장
    public void enterRoom(String roomId) {

        // 유저 조회
        Member member = memberRepository.findByUsername("hyungi")
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 방 조회
        Room room = roomRepository.getRoomData(roomId);

        // 방이 없는 경우 예외 처리


        // 방 입장
        room.addPlayer(member.getUsername());

        // 방 수정
        roomRepository.updateRoomData(roomId, room);
    }

    // 방 나가기
    public void leaveRoom(String roomId) {

        // 유저 조회
        Member member = memberRepository.findByUsername("hyungi")
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 방 조회
        Room room = roomRepository.getRoomData(roomId);

        // 방 나가기
        room.removePlayer(member.getUsername());

        // 방 나가고 플레이어가 남아있지 않으면 방 삭제
        if (room.getPlayerList().size() == 0) {
            roomRepository.deleteRoomData(roomId);
            return;
        }

        // 방장인 경우
        if (room.getHost().equals(member.getUsername()))
        {

            // 남은 인원 중에 임의로 한 명에게 방장 권한 위임
            roomRepository.delegateHost(roomId, room);
        }

        // 방 수정
        roomRepository.updateRoomData(roomId, room);
    }

    // 방 조회
    public RoomInfoResponseDTO getRoomInfo(String roomId) {
        // 방 조회
        Room room = roomRepository.getRoomData(roomId);

        return RoomInfoResponseDTO.of(room);
    }
}
