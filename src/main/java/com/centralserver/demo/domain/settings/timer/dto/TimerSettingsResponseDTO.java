package com.centralserver.demo.domain.settings.timer.dto;

import com.centralserver.demo.domain.settings.timer.entity.CountdownType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimerSettingsResponseDTO {

    private Boolean countdownEnabled;
    private CountdownType countdownType;
}