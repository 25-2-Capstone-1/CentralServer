package com.centralserver.demo.domain.route.entity;

import com.centralserver.demo.domain.googlemap.entity.StreetViewImage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedRoute {

    /**
     * 경로 ID (PK)
     * - String PK를 사용할 경우: Custom Generator 사용
     * - Long PK를 사용할 경우: IDENTITY 전략 사용
     *
     * 여기서는 String + Custom Generator 방식으로 구성하는 것을 기준으로 함
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @OneToMany(mappedBy = "recommendedRoute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StreetViewImage> streetViewImages = new ArrayList<>();

    /**
     * 경로 이름
     * 예: "한강 러닝 5KM 루트", "성산대교 ~ 월드컵경기장"
     */
    private String routeName;

    /**
     * 총 거리 (km 단위)
     */
    private double distance;

    /**
     * 예상 소요 시간 (초 또는 분 단위 — 서비스 요구에 따라 정의)
     */
    private int estimatedTime;

    /**
     * 난이도 (예: "Easy", "Moderate", "Hard")
     */
    private String difficulty;

    /**
     * 경로 설명
     * 예: "러닝 초보자에게 좋은 평지 기반 코스"
     */
    private String description;

    /**
     * 시작 지점, 끝 지점 이름 (옵션: UX 향상)
     */
    private String startPointName;
    private String endPointName;

    /**
     * 시작 지점, 끝 지점, 경유지 데이터를 JSON 문자열로 저장
     * - 위도/경도 정보 포함
     * - Front에서 Polyline, 지도 표시 등을 할 때 사용
     */
    @Lob
    private String startPointJson;

    @Lob
    private String endPointJson;

    @Lob
    private String waypointsJson;

    /**
     * 총 누적 고도
     */
    private Double totalElevationGain;

    /**
     * 생성 시간
     * - 추천 경로가 언제 만들어졌는지 추적
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
