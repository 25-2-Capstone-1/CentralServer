package com.centralserver.demo.domain.record.service;

import com.centralserver.demo.domain.record.dto.RunRecordRequestDTO;
import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.record.repository.RunRecordRepository;
import com.centralserver.demo.domain.user.entity.UserEntity;
import com.centralserver.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RunRecordService {

    private final UserRepository userRepository;
    private final RunRecordRepository runRecordRepository;


    /** 1) 저장(Create) */
    public RunRecordEntity createRecord(RunRecordRequestDTO dto, Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RunRecordEntity record = RunRecordEntity.builder()
                .user(user)
                .title(dto.getTitle())
                .startTime(dto.getStartTime())
                .durationSeconds(dto.getDurationSeconds())
                .distanceKm(dto.getDistanceKm())
                .avgPace(dto.getAvgPace())
                .calories(dto.getCalories())
                .elevationGain(dto.getElevationGain())
                .avgHeartRate(dto.getAvgHeartRate())
                .cadence(dto.getCadence())
                .fullAddress(dto.getFullAddress())
                .waypointsJson(dto.getWaypointsJson())
                .difficulty(dto.getDifficulty())
                .description(dto.getDescription())
                .bookmark(false)
                .build();

        return runRecordRepository.save(record);
    }

    /** 2) 전체 조회(Read) */
    public List<RunRecordEntity> getAllRecords(Long userId) {
        return runRecordRepository.findByUserId(userId);
    }

    /** 3) 북마크만 조회(Read) */
    public List<RunRecordEntity> getBookmarkedRecords(Long userId) {
        return runRecordRepository.findByUserIdAndBookmarkTrue(userId);
    }

    /** 4) 단일 조회(Read) */
    public RunRecordEntity getRecord(Long recordId) {
        return runRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다. ID=" + recordId));
    }

    /** 5) 수정(Update) */
    public RunRecordEntity updateRecord(Long recordId, RunRecordEntity updateData) {

        RunRecordEntity existing = runRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 존재하지 않습니다. ID=" + recordId));

        // 수정 가능한 필드만 갱신
        existing.setTitle(updateData.getTitle());
        existing.setStartTime(updateData.getStartTime());
        existing.setDurationSeconds(updateData.getDurationSeconds());
        existing.setDistanceKm(updateData.getDistanceKm());
        existing.setAvgPace(updateData.getAvgPace());
        existing.setCalories(updateData.getCalories());
        existing.setElevationGain(updateData.getElevationGain());
        existing.setAvgHeartRate(updateData.getAvgHeartRate());
        existing.setCadence(updateData.getCadence());
        existing.setWaypointsJson(updateData.getWaypointsJson());
        existing.setDifficulty(updateData.getDifficulty());
        existing.setDescription(updateData.getDescription());
        existing.setBookmark(updateData.isBookmark());

        return runRecordRepository.save(existing);
    }

    /** 6) 삭제(Delete) */
    public void deleteRecord(Long recordId) {
        RunRecordEntity existing = runRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 기록이 존재하지 않습니다. ID=" + recordId));

        runRecordRepository.delete(existing);
    }
}
