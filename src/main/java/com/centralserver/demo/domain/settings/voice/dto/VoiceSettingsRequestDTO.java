package com.centralserver.demo.domain.settings.voice.dto;

import com.centralserver.demo.domain.settings.voice.entity.VoiceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoiceSettingsRequestDTO {

    // 페이스 메이커
    private Boolean pacemakerEnabled;
    private String pacemakerTargetTime;        // "X:XX"
    private VoiceType voiceType;
    private Integer voiceFrequencyMinutes;

    // 네비게이션
    private Boolean navigationEnabled;
    private VoiceType navigationVoiceType;
}