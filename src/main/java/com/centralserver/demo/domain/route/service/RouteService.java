package com.centralserver.demo.domain.route.service;

// import com.centralserver.demo.domain.googlemap.service.StreetViewImageService;
import com.centralserver.demo.config.RoadviewConfig;
import com.centralserver.demo.domain.openai.service.GPTService;
import com.centralserver.demo.domain.openai.dto.RouteEnhanceResult;
import com.centralserver.demo.domain.route.client.RouteRecommendationClient;
import com.centralserver.demo.domain.route.dto.*;
import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import com.centralserver.demo.domain.route.repository.RecommendedRouteRepository;
import com.centralserver.demo.domain.route.client.dto.RouteJsonConverter;
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

    @Qualifier("routeRecommendationHttpClient")
    private final RouteRecommendationClient recommendationClient;

    private final RecommendedRouteRepository routeRepository;
    private final RouteJsonConverter jsonConverter;
    private final GPTService gptService;
    private final RoadviewConfig roadviewConfig;
    // private final StreetViewImageService streetViewImageService;

    /**
     * 추천 경로 생성 전체 과정:
     * - 외부 RawRoute 조회
     * - 예상 시간 계산
     * - GPT로 이름/난이도/설명 생성
     * - DB 저장
     * - 프론트 응답 생성
     */
    @Transactional
    public List<RecommendRouteDTO> recommendRoutes(@RequestBody RouteRequestDTO request) throws IOException {

        // 1. 외부(또는 Mock)에서 RawRoute 데이터 가져오기
        List<RecommendedRawRouteDTO> rawRoutes = recommendationClient.getRecommendedRoutes(request);

        List<RecommendRouteDTO> routes = new ArrayList<>();

        // 여러 개의 추천 코스를 생성
        for (RecommendedRawRouteDTO raw : rawRoutes) {

            // 2. 예상 시간 계산
            int estimatedTime = calculateEstimatedTime(raw.getDistance());

            // 3. GPT가 코스 이름·난이도·설명을 생성
            RouteEnhanceResult enhanced = gptService.callGptForEnhancement(raw, request);

            // 4. startToEnd 경로에서 대표 지점 3개 선정
            List<Point> representativePoints = pickRepresentativePoints(raw.getStartToEnd());

            // 5. 각 대표 지점에 대해 로드뷰 URL 생성
            List<RoadviewItem> roadviewItems = buildRoadviewItems(representativePoints);

            // 6. RecommendRouteDTO 생성
            RecommendRouteDTO dto = RecommendRouteDTO.builder()
                    .routeName(enhanced.getRouteName())
                    .myPoint(raw.getMyPoint())
                    .startPoint(raw.getStartPoint())
                    .endPoint(raw.getEndPoint())
                    .myToStart(raw.getMyToStart())
                    .startToEnd(raw.getStartToEnd())
                    .distance(raw.getDistance())
                    .estimatedTime(estimatedTime)
                    .difficulty(enhanced.getDifficulty())
                    .description(enhanced.getDescription())
                    .roadviews(roadviewItems)   // ✔ 대표 3개 로드뷰
                    .build();

            // 7. DB 저장 후 routeId 부여
            Long savedId = saveRecommendedRoute(dto);
            dto.setRouteId(savedId);

            routes.add(dto);
        }

        return routes;
    }

    private List<Point> pickRepresentativePoints(List<Point> points) {
        int size = points.size();

        if (size <= 3) {
            return points;  // 너무 적으면 전체 반환
        }

        Point p1 = points.get(size / 4);
        Point p2 = points.get(size / 2);
        Point p3 = points.get(size * 3 / 4);

        return List.of(p1, p2, p3);
    }

    private List<RoadviewItem> buildRoadviewItems(List<Point> points) {

        String baseUrl = roadviewConfig.getBaseUrl();

        List<RoadviewItem> items = new ArrayList<>();
        for (Point p : points) {
            String url = baseUrl + "?lat=" + p.lat() + "&lng=" + p.lng();
            items.add(new RoadviewItem(p.lat(), p.lng(), url));
        }
        return items;
    }

    /**
     * StreetView 이미지 생성
     */
//    private void generateStreetViewImages(Long routeId, List<Point> pathPoints) {
//
//        RecommendedRoute route = routeRepository.findById(routeId)
//                .orElseThrow(() -> new RuntimeException("Route not found"));
//
//        for (Point p : pathPoints) {
//            streetViewImageService.createAndSaveImage(
//                    route,
//                    p.lat(),
//                    p.lng()
//            );
//        }
//    }

    /**
     * DB 저장
     */
    private Long saveRecommendedRoute(RecommendRouteDTO dto) {

        RecommendedRoute entity = RecommendedRoute.builder()
                .routeName(dto.getRouteName())
                .distance(dto.getDistance())
                .estimatedTime(dto.getEstimatedTime())
                .difficulty(dto.getDifficulty())
                .description(dto.getDescription())

                .myPointJson(jsonConverter.myPointToJson(dto))
                .startPointJson(jsonConverter.startPointToJson(dto))
                .endPointJson(jsonConverter.endPointToJson(dto))
                .myToStartJson(jsonConverter.myToStartJson(dto))
                .startToEndJson(jsonConverter.startToEndJson(dto))
                .build();

        RecommendedRoute saved = routeRepository.save(entity);
        return saved.getRouteId();
    }

    /**
     * routeId로 단일 조회
     */
    public RecommendedRoute getRouteById(Long routeId) {
        return routeRepository.findByRouteId(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found: " + routeId));
    }

    /**
     * 특정 날짜 이후 생성된 추천 경로 조회
     */
    public List<RecommendedRoute> getRoutesAfter(LocalDateTime after) {
        return routeRepository.findByCreatedAtAfter(after);
    }

    /*
     * 계산 로직들
     */
    private int calculateEstimatedTime(double distance) {
        double walkingSpeed = 1.2; // 1.2 m/s
        return (int) (distance / walkingSpeed);
    }

    private String simplifyPoint(Point p) {
        String lat = String.format("%.2f", p.lat());
        String lon = String.format("%.2f", p.lng());
        return "(" + lat + ", " + lon + ")";
    }
}
