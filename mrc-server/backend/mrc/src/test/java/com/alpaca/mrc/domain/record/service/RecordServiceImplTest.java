package com.alpaca.mrc.domain.record.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.alpaca.mrc.domain.game.util.GameMode;
import com.alpaca.mrc.domain.game.util.GamePlayerNum;
import com.alpaca.mrc.domain.member.entity.Member;
import com.alpaca.mrc.domain.record.dto.response.RecordResponseDTO;
import com.alpaca.mrc.domain.record.entity.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class RecordServiceImplTest {

    @Mock
    private Member member = new Member(1L, null, null, null, "email1", "사용자1", "사용자닉1", "icon1", 1000, null, null, LocalDateTime.now(), LocalDateTime.now());

    @InjectMocks
    private RecordServiceImpl recordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Member setup
        Record record1 = Record.builder()
                .id(1L)
                .member(member)
                .lapTime(LocalDateTime.now())
                .distance(100)
                .avgSpeed(10)
                .gameMode(GameMode.TUTORIAL)
                .gamePlayerNum(GamePlayerNum.SINGLE)
                .isWin(true)
                .mapUrl("urlMap1")
                .replayUrl("urlReplay1")
                .build();

        Record record2 = Record.builder()
                .id(2L)
                .member(member)
                .lapTime(LocalDateTime.now())
                .distance(150)
                .avgSpeed(10)
                .gameMode(GameMode.SPEED)
                .gamePlayerNum(GamePlayerNum.MULTI_TWO)
                .isWin(false)
                .mapUrl("urlMap2")
                .replayUrl("urlReplay2")
                .build();

        List<Record> records = Arrays.asList(record1, record2);
        when(member.getRecords()).thenReturn(records);
    }

    @Test
    void testGetRecord() {
        // Act
        RecordResponseDTO result = recordService.getRecord();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getRecords().size());
        assertEquals(100, result.getRecords().get(0).getDistance());
        assertEquals(GameMode.TUTORIAL, result.getRecords().get(0).getGameMode());
        assertEquals(150, result.getRecords().get(1).getDistance());
        assertEquals(GameMode.SPEED, result.getRecords().get(1).getGameMode());
    }
}
