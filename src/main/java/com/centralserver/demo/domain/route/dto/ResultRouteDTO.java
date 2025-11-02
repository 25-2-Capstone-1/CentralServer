package com.centralserver.demo.domain.route.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultRouteDTO {

    private String routeId;           // 어떤 코스를 달렸는가
    private String userId;            // 사용자 ID
    private LocalDateTime startTime;  // 시작 시각
    private LocalDateTime endTime;    // 종료 시각
    private List<Point> waypoints; // 실제 달린 좌표 로그
    private double distance;          // 실제 이동 거리 (m)
    private int duration;             // 전체 시간 (초)
    private double averageSpeed;      // 평균 속도 (m/s)
    private double caloriesBurned;    // 칼로리 소모량 (kcal)

}