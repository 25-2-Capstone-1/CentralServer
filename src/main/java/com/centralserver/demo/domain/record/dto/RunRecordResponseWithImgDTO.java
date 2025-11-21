package com.centralserver.demo.domain.record.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunRecordResponseWithImgDTO {

    private Long id;

    private String title;

    private boolean bookmark;

    private LocalDateTime startTime;

    private int durationSeconds;

    private double distanceKm;

    private String avgPace;

    private Integer calories;

    private Integer cadence;

    private String fullAddress;

    private String waypointsJson;

    private Long recommendedRouteId;  // null 가능

    private String imageUrl;          // 🔥 추가됨 (S3 URL)
}
