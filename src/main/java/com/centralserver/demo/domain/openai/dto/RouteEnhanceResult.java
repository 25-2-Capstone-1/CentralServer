package com.centralserver.demo.domain.openai.dto;

import lombok.Data;

@Data
public class RouteEnhanceResult {
    private String difficulty;
    private String routeName;
    private String description;
}
