package com.centralserver.demo.domain.route.service;

import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import com.centralserver.demo.domain.route.repository.RecommendedRouteRepository;
import com.centralserver.demo.domain.route.repository.SelectedRouteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RecommendedRouteRepository recommendedRouteRepository;
    private final SelectedRouteRepository selectedRouteRepository;

    public RouteService(RecommendedRouteRepository recommendedRouteRepository, SelectedRouteRepository selectedRouteRepository) {
        this.recommendedRouteRepository = recommendedRouteRepository;
        this.selectedRouteRepository = selectedRouteRepository;
    }

    @Transactional
    public List<RecommendRouteDTO> recommendRoutes(@RequestBody RouteRequestDTO request) throws IOException {

        List<RecommendRouteDTO> routes = new ArrayList<>();

        // 현재는 미리 만들어 둔 json 파일 사용
        RecommendRouteDTO route1 = loadRouteFromJson("data/RecommendRoute/RecommendRoute1.json");
        RecommendRouteDTO route2 = loadRouteFromJson("data/RecommendRoute/RecommendRoute2.json");
        RecommendRouteDTO route3 = loadRouteFromJson("data/RecommendRoute/RecommendRoute3.json");

        routes.add(route1);
        routes.add(route2);
        routes.add(route3);

        return routes;
    }

    private RecommendRouteDTO loadRouteFromJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return objectMapper.readValue(resource.getInputStream(), RecommendRouteDTO.class);
    }

//    @Transactional
//    public void createSampleRoute() {
//
//        RecommendedRoute route = RecommendedRoute.builder()
//                .routeName("한강 자전거 코스")
//                .distance(5.2)
//                .estimatedTime(1800)
//                .difficulty("easy")
//                .description("한강을 따라 달리는 자전거 코스입니다.")
//                .startPointJson("{\"lat\":37.55,\"lng\":126.97}")
//                .endPointJson("{\"lat\":37.56,\"lng\":127.0}")
//                .waypointsJson("[{\"lat\":37.551,\"lng\":126.98},{\"lat\":37.552,\"lng\":126.99}]")
//                .build();
//
//        recommendedRouteRepository.save(route);
//    }


    //      선택된 내용 db 저장 로직 제거
//    @PostMapping("/result")
//    public ResultRouteDTO getSelectedRoute(@RequestBody RouteRequestDTO request) throws IOException {
//        // 사용자가 선택한 경로를 기준으로 최종 경로를 응답하는 예시
//        RecommendRouteDTO selectedRoute = loadRouteFromJson("data/RecommendRoute1.json");
//
//        ResultRouteDTO result = new ResultRouteDTO();
//        result.setRoute(selectedRoute);
//        result.setMessage("선택된 경로입니다.");
//        return result;
//    }
}