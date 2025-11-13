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

}