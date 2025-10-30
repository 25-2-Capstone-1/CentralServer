package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/recommend")
    public List<RecommendRouteDTO> recommendRoutes(@RequestBody RouteRequestDTO request) throws IOException {
        // 1️⃣ 요청값 출력 (디버깅용)
        System.out.println("추천 요청: " + request);

        // 2️⃣ JSON 파일 2개 읽기 (src/main/resources/data/ 경로)
        RecommendRouteDTO route1 = loadRouteFromJson("data/routes/route1.json");
        RecommendRouteDTO route2 = loadRouteFromJson("data/routes/route2.json");
        RecommendRouteDTO route3 = loadRouteFromJson("data/routes/route3.json");


        // 3️⃣ 여러 개의 추천 경로 반환
        List<RecommendRouteDTO> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);

        return routes;
    }

    private RecommendRouteDTO loadRouteFromJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return objectMapper.readValue(resource.getInputStream(), RecommendRouteDTO.class);
    }
//      선택된 내용 db 저장 로직 제거
//    @PostMapping("/result")
//    public ResultRouteDTO getSelectedRoute(@RequestBody RouteRequestDTO request) throws IOException {
//        // 사용자가 선택한 경로를 기준으로 최종 경로를 응답하는 예시
//        RecommendRouteDTO selectedRoute = loadRouteFromJson("data/route1.json");
//
//        ResultRouteDTO result = new ResultRouteDTO();
//        result.setRoute(selectedRoute);
//        result.setMessage("선택된 경로입니다.");
//        return result;
//    }
}