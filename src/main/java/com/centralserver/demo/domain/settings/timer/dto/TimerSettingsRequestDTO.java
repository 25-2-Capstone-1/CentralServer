package com.centralserver.demo.domain.settings.timer.dto;

import com.centralserver.demo.domain.settings.timer.entity.CountdownType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerSettingsRequestDTO {

    private Boolean countdownEnabled;
    private CountdownType countdownType;  // THREE_SECONDS / SIX_SECONDS / NINE_SECONDS
}