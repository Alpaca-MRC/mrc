package com.alpaca.mrc.domain.record.dto.response;

import com.alpaca.mrc.domain.record.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Builder
@Getter
@AllArgsConstructor
public class RecordResponseDTO {
    private List<Record> records;

    public static RecordResponseDTO of(List<Record> records) {
        return RecordResponseDTO.builder()
                .records(records)
                .build();
    }
}