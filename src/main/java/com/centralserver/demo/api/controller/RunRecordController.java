package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.common.dto.ApiResponseDTO;
import com.centralserver.demo.domain.common.dto.MessageResponseDTO;
import com.centralserver.demo.domain.record.dto.RunRecordRequestDTO;
import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.record.service.RunRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RunRecordController {

    private final RunRecordService runRecordService;

    /** 1) 기록 생성 */
    @PostMapping("/record")
    public ResponseEntity<ApiResponseDTO<RunRecordEntity>> createRecord(
            @RequestBody RunRecordRequestDTO request,
            Authentication authentication
    ) {

        RunRecordEntity saved = runRecordService.createRecord(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(saved));
    }
}
