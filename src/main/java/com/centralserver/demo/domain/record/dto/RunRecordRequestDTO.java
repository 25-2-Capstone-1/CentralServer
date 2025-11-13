package com.centralserver.demo.domain.record.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RunRecordRequestDTO {

    private String title;

    private LocalDateTime startTime;

    private int durationSeconds;

    private double distanceKm;

    private String avgPace;

    private Integer calories;

    private Double elevationGain;

    private Integer avgHeartRate;

    private Integer cadence;

    private String fullAddress;

    private String waypointsJson;

    private String difficulty;

    private String description;

    // ❌ 삭제: private Long userId;
}
