package com.centralserver.demo.domain.route.client;

import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import com.centralserver.demo.domain.route.dto.RecommendedRawRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;

import java.io.IOException;
import java.util.List;

public interface RouteRecommendationClient {
    List<RecommendedRawRouteDTO> getRecommendedRoutes(RouteRequestDTO request) throws IOException;
}