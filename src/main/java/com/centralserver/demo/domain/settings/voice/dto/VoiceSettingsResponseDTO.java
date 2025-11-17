package com.centralserver.demo.domain.settings.voice.dto;

import com.centralserver.demo.domain.settings.voice.entity.VoiceType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoiceSettingsResponseDTO {

    private Boolean pacemakerEnabled;
    private String pacemakerTargetTime;
    private VoiceType voiceType;
    private Integer voiceFrequencyMinutes;

    private Boolean navigationEnabled;
    private VoiceType navigationVoiceType;
}