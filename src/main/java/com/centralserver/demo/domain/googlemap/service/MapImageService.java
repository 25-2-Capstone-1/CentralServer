package com.centralserver.demo.domain.googlemap.service;

import com.centralserver.demo.domain.S3Service;
import com.centralserver.demo.domain.googlemap.GoogleMapImageDownloader;
import com.centralserver.demo.domain.googlemap.GoogleMapUrlBuilder;
import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.record.repository.RunRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapImageService {

    private final GoogleMapUrlBuilder urlBuilder;
    private final GoogleMapImageDownloader downloader;
    private final S3Service s3Service;
    private final RunRecordRepository runRecordRepository;

    public String createAndUploadMapImage(Long runRecordId, List<double[]> waypoints) throws Exception {

        // 1) URL 만들기
        String url = urlBuilder.buildStaticMapURL(waypoints);

        // 2) 구글 Static Map 이미지 다운로드
        byte[] imageBytes = downloader.download(url);

        // 3) S3 Key 만들기
        String key = "run-records/" + runRecordId + ".png";

        // 4) S3에 저장 (여기서 기존 S3Service 재사용)
        String imageUrl = s3Service.uploadBytes(imageBytes, key, "image/png");

        // 5) DB 업데이트
        RunRecordEntity record = runRecordRepository.findById(runRecordId)
                .orElseThrow(() -> new RuntimeException("RunRecord not found"));

        record.setImageUrl(imageUrl);
        runRecordRepository.save(record);

        return imageUrl;
    }
}
