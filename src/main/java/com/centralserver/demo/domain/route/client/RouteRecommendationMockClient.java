package com.centralserver.demo.domain.route.client;

import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import com.centralserver.demo.domain.route.dto.RecommendedRawRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteRecommendationMockClient implements RouteRecommendationClient {

    private final ObjectMapper objectMapper;

    @Override
    public List<RecommendedRawRouteDTO> getRecommendedRoutes(RouteRequestDTO request) throws IOException {

        List<RecommendedRawRouteDTO> rawRoutes = new ArrayList<>();

        rawRoutes.add(loadMockJson("data/RecommendRoute/RecommendRoute1.json"));
        rawRoutes.add(loadMockJson("data/RecommendRoute/RecommendRoute2.json"));
        rawRoutes.add(loadMockJson("data/RecommendRoute/RecommendRoute3.json"));

        return rawRoutes;
    }

    private RecommendedRawRouteDTO loadMockJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return objectMapper.readValue(resource.getInputStream(), RecommendedRawRouteDTO.class);
    }
}
