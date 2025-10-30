package com.centralserver.demo.domain.route.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendRouteDTO {

    private String routeId;          // 경로 식별자
    private String routeName;        // 경로 이름
    private Point startPoint;        // 시작 지점
    private Point endPoint;          // 도착 지점
    private List<Point> waypoints;   // 경유지 좌표 목록
    private double distance;         // 전체 거리 (m)
    private int estimatedTime;       // 예상 소요 시간 (초)
    private String difficulty;       // 난이도 (easy, medium, hard)
    private String description;      // 설명

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point {
        private double lat;
        private double lng;
    }
}
