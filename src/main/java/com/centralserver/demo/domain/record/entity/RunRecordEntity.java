package com.centralserver.demo.domain.record.entity;

import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import com.centralserver.demo.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "run_record")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunRecordEntity {

    /** 기본 식별자 **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자 정보 (사용자별 기록 관리용) **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /** 추천된 경로 정보 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_route_id")
    private RecommendedRoute recommendedRoute;

    /** 기본 정보 **/
    @Column(nullable = false, length = 100)
    private String title;                 // 러닝 제목

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;      // 시작 시각

    @Column(name = "duration_seconds", nullable = false)
    private int durationSeconds;          // 운동 시간 (초)

    @Column(name = "distance_km", nullable = false)
    private double distanceKm;            // 총 거리 (km)

    @Column(name = "avg_pace", length = 20)
    private String avgPace;               // 평균 페이스 ("6'18''")

    @Column
    private Integer calories;             // 칼로리 소모량

    @Column(name = "elevation_gain")
    private Double elevationGain;         // 총 상승 고도 (m)

    @Column(name = "avg_heart_rate")
    private Integer avgHeartRate;         // 평균 심박수

    @Column
    private Integer cadence;              // 케이던스

    /** 전체 주소 (읍/면/동까지 포함한 지명 전체 문자열) **/
    @Column(name = "full_address", length = 255)
    private String fullAddress;

    /** 경로 정보 (JSON) **/
    @Column(name = "waypoints_json", columnDefinition = "JSON")
    private String waypointsJson;

    /** bookmark 여부(찜/즐겨찾기) **/
    @Builder.Default
    @Column(name = "bookmark", nullable = false)
    private boolean bookmark = false;

    /** 생성/수정 시각 **/
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}