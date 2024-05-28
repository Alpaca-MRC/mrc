package com.alpaca.mrc.domain.room.controller;

import com.alpaca.mrc.domain.member.dto.request.AuthRequestDTO;
import com.alpaca.mrc.domain.room.dto.request.RoomCreateRequestDTO;
import com.alpaca.mrc.domain.room.dto.request.RoomEnterRequestDTO;
import com.alpaca.mrc.domain.room.dto.request.RoomInfoRequestDTO;
import com.alpaca.mrc.domain.room.dto.request.RoomLeaveRequestDTO;
import com.alpaca.mrc.domain.room.service.RoomServiceImpl;
import com.alpaca.mrc.global.result.ResultCode;
import com.alpaca.mrc.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
@Slf4j
public class RoomController {

    private final RoomServiceImpl roomService;

    // 방 생성
    @Operation(summary = "방 생성")
    @PostMapping("/create")
    public ResponseEntity<ResultResponse> createRoom(@RequestBody RoomCreateRequestDTO requestDTO) {
        ;
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ROOM_CREATE_SUCCESS, roomService.createRoom(requestDTO.getMaxPlayerNumber())));
    }

    // 방 입장
    @Operation(summary = "방 입장")
    @PostMapping("/enter")
    public ResponseEntity<ResultResponse> enterRoom(@RequestBody RoomEnterRequestDTO requestDto) {
        roomService.enterRoom(requestDto.getRoomId());
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ROOM_ENTER_SUCCESS));
    }

    // 방 나가기
    @Operation(summary = "방 퇴장")
    @PostMapping("/leave")
    public ResponseEntity<ResultResponse> leaveRoom(@RequestBody RoomLeaveRequestDTO requestDto) {
        roomService.leaveRoom(requestDto.getRoomId());
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ROOM_LEAVE_SUCCESS));
    }

    // 방 조회
    @Operation(summary = "방 조회")
    @GetMapping("/{roomId}")
    public ResponseEntity<ResultResponse> getRoomInfo(@PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.ROOM_GET_INFO_SUCCESS, roomService.getRoomInfo(roomId)));
    }
}
