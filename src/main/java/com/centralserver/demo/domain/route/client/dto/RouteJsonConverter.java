package com.centralserver.demo.domain.route.client.dto;

import com.centralserver.demo.domain.route.dto.RecommendRouteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class RouteJsonConverter {

    private final ObjectMapper objectMapper;

    /** 공통 JSON 직렬화 메서드 */
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패: " + e.getMessage(), e);
        }
    }

    /** 내 위치(JSON) */
    public String myPointToJson(RecommendRouteDTO dto) {
        return toJson(dto.getMyPoint());
    }

    /** 시작점(JSON) */
    public String startPointToJson(RecommendRouteDTO dto) {
        return toJson(dto.getStartPoint());
    }

    /** 종료점(JSON) */
    public String endPointToJson(RecommendRouteDTO dto) {
        return toJson(dto.getEndPoint());
    }

    /** 내 위치 → 시작점 경로(JSON) */
    public String myToStartJson(RecommendRouteDTO dto) {
        return toJson(dto.getMyToStart());
    }

    /** 시작점 → 종료점 경로(JSON) */
    public String startToEndJson(RecommendRouteDTO dto) {
        return toJson(dto.getStartToEnd());
    }
}
