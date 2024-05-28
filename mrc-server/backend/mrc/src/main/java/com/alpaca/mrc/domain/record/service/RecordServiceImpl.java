package com.alpaca.mrc.domain.record.service;

import com.alpaca.mrc.domain.game.util.GameMode;
import com.alpaca.mrc.domain.game.util.GamePlayerNum;
import com.alpaca.mrc.domain.member.entity.Member;
import com.alpaca.mrc.domain.member.repository.MemberRepository;
import com.alpaca.mrc.domain.record.dto.response.RecordResponseDTO;
import com.alpaca.mrc.domain.record.entity.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl {

    private final MemberRepository memberRepository;

    // TODO: 테스트용 임시 코드
    Record record1 = new Record(1L,null, LocalDateTime.now(), 100, 50, GameMode.TUTORIAL, GamePlayerNum.SINGLE, true, "urlMap1", "urlReplay1", null);
    Record record2 = new Record(2L,null, LocalDateTime.now(), 150, 55, GameMode.SPEED, GamePlayerNum.MULTI_TWO, false, "urlMap2", "urlReplay2", null);
    List<Record> records = Arrays.asList(record1, record2);

    private final Member member = Member.builder()
            .id(1L)
            .records(records)
            .username("김주피")
            .nickname("테스트")
            .iconUrl("s3.com")
            .coin(999)
            .build();

    // 전적 조회
    public RecordResponseDTO getRecord() {

        // 전적 가져오기
        List<Record> records = member.getRecords();
        return RecordResponseDTO.of(records);
    }
}
