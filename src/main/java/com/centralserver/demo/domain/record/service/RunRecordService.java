
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
}
