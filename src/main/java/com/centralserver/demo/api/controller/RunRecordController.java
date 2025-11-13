package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.record.dto.RunRecordRequestDTO;
import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.record.service.RunRecordService;
import com.centralserver.demo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> createRecord(
            @RequestBody RunRecordRequestDTO request,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        RunRecordEntity saved = runRecordService.createRecord(request, userId);
        return ResponseEntity.ok(saved);
    }

//    /** 2) 전체 기록 조회 (사용자 기준) */
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<RunRecordEntity>> getAllRecords(
//            @PathVariable Long userId
//    ) {
//        List<RunRecordEntity> records = runRecordService.getAllRecords(userId);
//        return ResponseEntity.ok(records);
//    }
//
//    /** 3) 북마크 된 기록만 조회 */
//    @GetMapping("/user/{userId}/bookmark")
//    public ResponseEntity<List<RunRecordEntity>> getBookmarkedRecords(
//            @PathVariable Long userId
//    ) {
//        List<RunRecordEntity> bookmarked = runRecordService.getBookmarkedRecords(userId);
//        return ResponseEntity.ok(bookmarked);
//    }
//
//    /** 4) 단일 기록 조회 */
//    @GetMapping("/{id}")
//    public ResponseEntity<RunRecordEntity> getRecord(
//            @PathVariable Long id
//    ) {
//        RunRecordEntity record = runRecordService.getRecord(id);
//        return ResponseEntity.ok(record);
//    }
//
//    /** 5) 기록 수정 */
//    @PutMapping("/{id}")
//    public ResponseEntity<RunRecordEntity> updateRecord(
//            @PathVariable Long id,
//            @RequestBody RunRecordEntity updateData
//    ) {
//        RunRecordEntity updated = runRecordService.updateRecord(id, updateData);
//        return ResponseEntity.ok(updated);
//    }
//
//    /** 6) 기록 삭제 */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteRecord(
//            @PathVariable Long id
//    ) {
//        runRecordService.deleteRecord(id);
//        return ResponseEntity.ok("기록이 삭제되었습니다. ID=" + id);
//    }
}
