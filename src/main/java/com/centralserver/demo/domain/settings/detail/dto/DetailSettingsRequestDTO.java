package com.centralserver.demo.domain.settings.detail.dto;


import com.centralserver.demo.domain.settings.detail.entity.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailSettingsRequestDTO {
    private Gender gender;
    private Integer height;
    private Double weight;
}
