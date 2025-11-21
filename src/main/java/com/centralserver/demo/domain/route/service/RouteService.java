package com.centralserver.demo.domain.route.service;

import com.centralserver.demo.domain.googlemap.service.StreetViewImageService;
import com.centralserver.demo.domain.openai.service.GPTService;
import com.centralserver.demo.domain.openai.dto.RouteEnhanceResult;
import com.centralserver.demo.domain.route.client.RouteRecommendationClient;
import com.centralserver.demo.util.RouteJsonConverter;
import com.centralserver.demo.domain.route.dto.Point;
import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import com.centralserver.demo.domain.route.dto.RecommendedRawRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import com.centralserver.demo.domain.route.repository.RecommendedRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    @Qualifier("routeRecommendationMockClient")
    private final RouteRecommendationClient recommendationClient;

    private final RecommendedRouteRepository routeRepository;
    private final RouteJsonConverter jsonConverter;
    private final GPTService gptService;
    private final StreetViewImageService streetViewImageService;

    /**
     * - 외부 추천 API 호출
     * - 난이도 계산
     * - 설명 생성
     * - DB 저장
     * - 프론트 응답 DTO 반환
     */

    /**
     * 1. 추천 루트 생성
     * 추천 경로 여러 개 생성 후 바로 프론트로 반환
     */

    @Transactional
    public List<RecommendRouteDTO> recommendRoutes(@RequestBody RouteRequestDTO request) throws IOException {

        // 1. 추천 클라이언트 서버에서 기본 정보만 받아오기
        // startPoint, endPoint, waypoints, distance, estimatedTime(없을 수도 있음)
        List<RecommendedRawRouteDTO> rawRoutes = recommendationClient.getRecommendedRoutes(request);

        List<RecommendRouteDTO> routes = new ArrayList<>();

        for (RecommendedRawRouteDTO raw : rawRoutes) {

            // 2. 중간 서버에서 생성하는 필드
            int estimatedTime = calculateEstimatedTime(raw.getDistance());
//            String difficulty = calculateDifficulty(raw.getDistance(), request.slope());
//            String routeName = generateRouteName(raw.getStartPoint(), raw.getEndPoint(), raw.getDistance());
//            String description = generateDescription(difficulty);

            RouteEnhanceResult enhanced = gptService.callGptForEnhancement(raw, request);

            // 가공된 DTO 생성
            RecommendRouteDTO dto = RecommendRouteDTO.builder()
                    .routeName(enhanced.getRouteName())
                    .startPoint(raw.getStartPoint())
                    .endPoint(raw.getEndPoint())
                    .waypoints(raw.getWaypoints())
                    .distance(raw.getDistance())
                    .estimatedTime(estimatedTime)
                    .difficulty(enhanced.getDifficulty())
                    .description(enhanced.getDescription())
                    .build();

            // DB 저장 , ID 반환
            Long savedId = saveRecommendedRoute(dto);
            dto.setRouteId(savedId);


            /** 🔥 Street View 이미지 생성 + 저장 */
            generateStreetViewImages(savedId, raw.getWaypoints());



            // 프론트로 보낼 결과 리스트에 추가
            routes.add(dto);
        }

        // 3. 그대로 프론트에 반환
        return routes;
    }


    private void generateStreetViewImages(Long routeId, List<Point> waypoints) {

        RecommendedRoute route =
                routeRepository.findById(routeId)
                        .orElseThrow(() -> new RuntimeException("Route not found"));

        // 🔥 필요한 경우 여러 지점 찍기
        for (Point p : waypoints) {
            streetViewImageService.createAndSaveImage(
                    route,
                    p.lat(),
                    p.lng()
            );
        }
    }



    /**
     * DB 저장 (원하는 경우)
     */
    private Long saveRecommendedRoute(RecommendRouteDTO dto) {
        RecommendedRoute entity = RecommendedRoute.builder()
                .routeName(dto.getRouteName())
                .distance(dto.getDistance())
                .estimatedTime(dto.getEstimatedTime())
                .difficulty(dto.getDifficulty())
                .description(dto.getDescription())
                .startPointJson(jsonConverter.startPointToJson(dto))
                .endPointJson(jsonConverter.endPointToJson(dto))
                .waypointsJson(jsonConverter.waypointsToJson(dto))
                .totalElevationGain(0.0)
                .build();

        RecommendedRoute saved = routeRepository.save(entity);
        return saved.getRouteId();   // ⭐ 자동 생성된 Primary Key 반환
    }

    /**
     * 2. routeId로 단일 조회
     */
    public RecommendedRoute getRouteById(Long routeId) {
        return routeRepository.findByRouteId(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found: " + routeId));
    }

    /**
     * 3. 특정 날짜 이후 생성된 추천 경로 조회
     */
    public List<RecommendedRoute> getRoutesAfter(LocalDateTime after) {
        return routeRepository.findByCreatedAtAfter(after);
    }




    /*
    *
    * 예상 시간 설정, 난이도 설정, 제목 설정, 설명 작성
    *
     */
    // 예상 시간 작성
    private int calculateEstimatedTime(double distance) {
        double walkingSpeed = 1.2; // 1.2 m/s
        return (int) (distance / walkingSpeed);
    }

    // 제목 생성
    private String generateRouteName(Point start, Point end, double distance) {

        // 거리 → km 변환 (소수점 1자리까지)
        double km = distance / 1000.0;
        String kmText = String.format("%.1fkm", km);

        // startPoint / endPoint에 이름이 없으므로 "출발지 → 도착지" 대신 좌표축약 표현
        String startLabel = simplifyPoint(start);
        String endLabel = simplifyPoint(end);

        // 제목 패턴
        return startLabel + " → " + endLabel + " " + kmText + " 러닝 코스";
    }

    private String simplifyPoint(Point p) {
        String lat = String.format("%.2f", p.lat());
        String lon = String.format("%.2f", p.lng());
        return "(" + lat + ", " + lon + ")";
    }

    // 난이도 설정
    private String calculateDifficulty(double distance, int slope) {
        if (distance < 3_000 && slope < 3) return "easy";
        if (distance < 7_000 && slope < 6) return "medium";
        return "hard";
    }

    // 설명 작성
    private String generateDescription(String diff) {
        return switch (diff) {
            case "easy" -> "초보자도 편하게 달릴 수 있는 쉬운 난이도의 코스입니다.";
            case "medium" -> "적당한 난이도로 지루하지 않게 달릴 수 있어요.";
            case "hard" -> "지구력과 체력을 필요로 하는 고난이도 러닝 코스입니다.";
            default -> "러닝 코스 설명이 준비되었습니다.";
        };
    }

}