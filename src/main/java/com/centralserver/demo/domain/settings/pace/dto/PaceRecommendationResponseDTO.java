package com.centralserver.demo.domain.settings.pace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaceRecommendationResponseDTO {
    private String beginner;
    private String intermediate;
    private String advanced;
}