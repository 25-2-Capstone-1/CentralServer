package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import com.centralserver.demo.domain.route.service.RouteService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    /**
     * 1. 추천 루트 생성 (추천 서버 호출 → DB 저장 → 프론트 응답)
     */
    @PostMapping("/recommend")
    public ResponseEntity<List<RecommendRouteDTO>> recommendRoutes(
            @RequestBody RouteRequestDTO request
    ) throws IOException {

        List<RecommendRouteDTO> results = routeService.recommendRoutes(request);

        return ResponseEntity.ok(results);
    }


    /**
     * 2. routeId로 추천 경로 단일 조회
     */
    @GetMapping("/{routeId}")
    public ResponseEntity<RecommendedRoute> getRouteById(
            @PathVariable Long routeId
    ) {
        RecommendedRoute route = routeService.getRouteById(routeId);
        return ResponseEntity.ok(route);
    }


    /**
     * 3. 생성 날짜 기준 조회
     * 예: /api/routes?after=2024-12-01T00:00:00
     */
    @GetMapping
    public ResponseEntity<List<RecommendedRoute>> getRoutesAfter(
            @RequestParam LocalDateTime after
    ) {
        List<RecommendedRoute> routes = routeService.getRoutesAfter(after);
        return ResponseEntity.ok(routes);
    }
}