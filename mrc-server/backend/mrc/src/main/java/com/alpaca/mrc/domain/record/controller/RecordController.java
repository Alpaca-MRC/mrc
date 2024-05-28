package com.alpaca.mrc.domain.record.controller;

import com.alpaca.mrc.domain.record.service.RecordServiceImpl;
import com.alpaca.mrc.global.result.ResultCode;
import com.alpaca.mrc.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
@Slf4j
public class RecordController {

    private final RecordServiceImpl recordService;

    @Operation(summary = "전적 조회")
    @GetMapping("/")
    public ResponseEntity<ResultResponse> getRecord() {

        return ResponseEntity.ok(ResultResponse.of(ResultCode.RECORD_GET_INFO_SUCCESS, recordService.getRecord()));
    }
}
