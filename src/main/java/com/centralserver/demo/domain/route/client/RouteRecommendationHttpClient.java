//package com.centralserver.demo.domain.route.client;
//
//import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
//import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RouteRecommendationHttpClient implements RouteRecommendationClient {
//
//    private final RestTemplate restTemplate;
//
//    // 추천 서버 URL (나중에 application.yml에서 주입 예정)
//    private final String RECOMMEND_API_URL = "http://recommendation-server/api/routes/recommend";
//
//    @Override
//    public List<RecommendRouteDTO> getRecommendedRoutes(RouteRequestDTO request) {
//
//        // 🔥 1) 추천 서버로 POST 요청 보내기
//        RecommendRouteDTO[] response = restTemplate.postForObject(
//                RECOMMEND_API_URL,
//                request,
//                RecommendRouteDTO[].class
//        );
//
//        // 🔥 2) Null-safe 리스트 변환
//        return response != null ? Arrays.asList(response) : List.of();
//    }
//}
