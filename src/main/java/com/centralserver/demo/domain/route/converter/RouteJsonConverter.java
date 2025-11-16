package com.centralserver.demo.domain.route.converter;

import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteJsonConverter {

    private final ObjectMapper objectMapper;

    /**
     * 일반적인 JSON 변환
     */
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패: " + e.getMessage());
        }
    }

    public String startPointToJson(RecommendRouteDTO dto) {
        return toJson(dto.getStartPoint());
    }

    /**
     * DTO의 종료 지점을 JSON으로 변환
     */
    public String endPointToJson(RecommendRouteDTO dto) {
        return toJson(dto.getEndPoint());
    }

    /**
     * DTO의 waypoint 리스트를 JSON으로 변환
     */
    public String waypointsToJson(RecommendRouteDTO dto) {
        return toJson(dto.getWaypoints());
    }
}
