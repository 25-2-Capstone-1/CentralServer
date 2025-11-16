
package com.centralserver.demo.domain.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RunRecordRequestDTO {

    /** 사용자 선택: 추천 경로 ID (optional) **/
    private Long recommendedRouteId;

    /** 기본 러닝 정보 **/
    @NotBlank
    private String title;                // 러닝 제목

    @NotNull
    private LocalDateTime startTime;     // 시작 시각

    @Positive
    private int durationSeconds;         // 운동 시간 (초)

    @Positive
    private double distanceKm;           // 총 거리 (km)

    private String avgPace;              // 평균 페이스 ("6'18''")

    private Integer calories;            // 칼로리 소모량

    private Double elevationGain;        // 총 상승 고도 (m)

    private Integer avgHeartRate;        // 평균 심박수

    private Integer cadence;             // 케이던스

    /** 주소 정보 **/
    private String fullAddress;

    /** 경로 정보(JSON) → server에서 그대로 저장 */
    private String waypointsJson;
}
