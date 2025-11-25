package com.centralserver.demo.domain.route.client.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalRouteItem {
    private ExternalCourseInfo course_info;   // startPoint / endPoint / distance
    private JsonNode my2start;                // 그래프호퍼 raw JSON
    private JsonNode start2end;               // 그래프호퍼 raw JSON
}