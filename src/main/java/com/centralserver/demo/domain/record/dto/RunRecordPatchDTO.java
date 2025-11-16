package com.centralserver.demo.domain.record.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunRecordPatchDTO {

    private String title;
    private java.time.LocalDateTime startTime;
    private Integer durationSeconds;
    private Double distanceKm;
    private String avgPace;
    private Integer calories;
    private Double elevationGain;
    private Integer avgHeartRate;
    private Integer cadence;
    private String fullAddress;
    private String waypointsJson;
    private String difficulty;
    private String description;
}
