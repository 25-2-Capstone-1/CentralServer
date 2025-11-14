
package com.centralserver.demo.domain.record.service;

import com.centralserver.demo.domain.record.dto.RunRecordRequestDTO;
import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.record.repository.RunRecordRepository;
import com.centralserver.demo.domain.user.entity.UserEntity;
import com.centralserver.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RunRecordService {

    private final UserRepository userRepository;
    private final RunRecordRepository runRecordRepository;

    /** 로그인한 사용자 가져오기 */
    private UserEntity getSessionUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized access.");
        }

        String email = auth.getName();

        return userRepository.findByUserEmailAndIsLock(email, false)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /** 1) 저장(Create) */
    public RunRecordEntity createRecord(RunRecordRequestDTO dto) {

        // 1. SecurityContext 에서 이메일 꺼내기
        UserEntity user = getSessionUser();

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

    /** 2) 단일 기록 조회(Read One) */
    public RunRecordEntity getRecord(Long recordId) throws AccessDeniedException {
        UserEntity user = getSessionUser();

        RunRecordEntity record = runRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found."));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to view this record.");
        }

        return record;
    }

    /** 3) 본인 기록 전체 조회(Read All) */
    public List<RunRecordEntity> getMyRecords() {
        UserEntity user = getSessionUser();

        return runRecordRepository.findAllByUser_Id(user.getId());
    }

    /** -------- UPDATE 영역 -------- */

    /** 4) 기록 수정(Update) */
    public RunRecordEntity updateRecord(Long recordId, RunRecordRequestDTO dto) throws AccessDeniedException {
        UserEntity user = getSessionUser();

        RunRecordEntity record = runRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found."));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to update this record.");
        }

        // 필요한 필드만 수정
        record.setTitle(dto.getTitle());
        record.setStartTime(dto.getStartTime());
        record.setDurationSeconds(dto.getDurationSeconds());
        record.setDistanceKm(dto.getDistanceKm());
        record.setAvgPace(dto.getAvgPace());
        record.setCalories(dto.getCalories());
        record.setElevationGain(dto.getElevationGain());
        record.setAvgHeartRate(dto.getAvgHeartRate());
        record.setCadence(dto.getCadence());
        record.setFullAddress(dto.getFullAddress());
        record.setWaypointsJson(dto.getWaypointsJson());
        record.setDifficulty(dto.getDifficulty());
        record.setDescription(dto.getDescription());

        return runRecordRepository.save(record);
    }

    /** -------- DELETE 영역 -------- */

    /** 5) 기록 삭제(Delete) */
    public void deleteRecord(Long recordId) throws AccessDeniedException {
        UserEntity user = getSessionUser();

        RunRecordEntity record = runRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found."));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this record.");
        }

        runRecordRepository.delete(record);
    }
}
