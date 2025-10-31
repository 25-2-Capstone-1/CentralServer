package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
import com.centralserver.demo.domain.route.service.RouteService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/recommend")
    public List<RecommendRouteDTO> recommendRoutes(@RequestBody RouteRequestDTO request) throws IOException {
        List<RecommendRouteDTO> routes = routeService.recommendRoutes(request);
        return routes;
    }

}