package com.alpaca.mrc.domain.record.service;

import com.alpaca.mrc.domain.record.dto.response.RecordResponseDTO;

public interface RecordService {

    // 전적 조회
    RecordResponseDTO getRecord();
}
