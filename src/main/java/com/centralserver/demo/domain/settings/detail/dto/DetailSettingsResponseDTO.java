package com.centralserver.demo.domain.settings.detail.dto;

import com.centralserver.demo.domain.settings.detail.entity.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailSettingsResponseDTO {
    private Gender gender;
    private Integer height;
    private Double weight;
}
