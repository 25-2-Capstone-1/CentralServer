package com.centralserver.demo.domain.route.dto;

public record RouteRequestDTO(
        Point myPoint,           // 사용자의 현재 위치
        Point startPoint,        // 코스 시작점
        Point endPoint,          // 코스 종료점
        double targetDistance,   // 목표 거리 (m)
        int slope,               // 경사도 선호도 (0~10)
        int trafficLights,       // 신호등 혼잡도 선호도 (0~10)
        int trafficCongestion    // 교통 혼잡도 선호도 (0~10)
) {
    public record Point(double lat, double lng) {}
}
