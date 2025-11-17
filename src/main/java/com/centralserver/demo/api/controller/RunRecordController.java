package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.common.dto.ApiResponseDTO;
import com.centralserver.demo.domain.common.dto.MessageResponseDTO;
import com.centralserver.demo.domain.record.dto.*;
import com.centralserver.demo.domain.record.dto.RunRecordRequestDTO;
import com.centralserver.demo.domain.record.dto.RunRecordResponseDTO;
import com.centralserver.demo.domain.record.dto.RunRecordUpdateDTO;
import com.centralserver.demo.domain.record.service.RunRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RunRecordController {

    private final RunRecordService runRecordService;

    /** =========================
     *   1) 기록 생성 (Create)
     *  POST /record
     * ========================= */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<RunRecordResponseDTO>> createRecord(
            @RequestBody RunRecordRequestDTO request
    ) {

        RunRecordResponseDTO saved = runRecordService.createRecord(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(saved));
    }

    /** =========================
     *   2) 단일 기록 조회 (Read One)
     *  GET /record/{id}
     * ========================= */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RunRecordResponseDTO>> getRecord(@PathVariable Long id) throws Exception {

        RunRecordResponseDTO record = runRecordService.getRecord(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(record));
    }

    /** =========================
     *   3) 사용자 전체 기록 조회 (Read All)
     *  GET /record/my
     * ========================= */
    @GetMapping("/my")
    public ResponseEntity<ApiResponseDTO<List<RunRecordSimpleResponseDTO>>> getMyRecords() {

        List<RunRecordSimpleResponseDTO> records = runRecordService.getMyRecords();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(records));
    }

    /** =========================
     *   4) 기록 수정 (Update)
     *  PUT /record/{id}
     * ========================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RunRecordResponseDTO>> updateRecord(
            @PathVariable Long id,
            @RequestBody RunRecordUpdateDTO request
    ) throws Exception {

        RunRecordResponseDTO updated = runRecordService.updateRecord(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(updated));
    }

    /** =========================
     *   5) 기록 삭제 (Delete)
     *  DELETE /record/{id}
     * ========================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MessageResponseDTO>> deleteRecord(@PathVariable Long id) throws Exception {

        runRecordService.deleteRecord(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(new MessageResponseDTO("Record deleted successfully.")));
    }

    /** =========================
     *   6) 북마크된 기록만 조회
     *  GET /records/my/bookmarks
     * ========================= */
    @GetMapping("/my/bookmarks")
    public ResponseEntity<ApiResponseDTO<List<RunRecordSimpleResponseDTO>>> getMyBookmarkedRecords() {

        List<RunRecordSimpleResponseDTO> records = runRecordService.getMyBookmarkedRecords();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(records));
    }
}
