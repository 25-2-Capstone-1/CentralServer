package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class UploadController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadTest(@RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }

        // 파일명 안전하게 처리
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            originalName = "unknown.png";
        }

        // key 생성
        String key = "test/" + UUID.randomUUID() + "-" + originalName;

        // 업로드 실행
        String url = s3Service.upload(file, key);

        // JSON 형태로 응답
        return ResponseEntity.ok().body(
                new UploadResponse(url, key)
        );
    }

    // 내부 클래스 형태로 JSON 응답
    record UploadResponse(String url, String key) {}
}
