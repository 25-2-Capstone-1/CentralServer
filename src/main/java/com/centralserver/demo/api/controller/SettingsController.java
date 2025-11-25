package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.settings.detail.dto.DetailSettingsRequestDTO;
import com.centralserver.demo.domain.settings.detail.dto.DetailSettingsResponseDTO;
import com.centralserver.demo.domain.settings.timer.dto.TimerSettingsRequestDTO;
import com.centralserver.demo.domain.settings.timer.dto.TimerSettingsResponseDTO;
import com.centralserver.demo.domain.settings.voice.dto.VoiceSettingsRequestDTO;
import com.centralserver.demo.domain.settings.voice.dto.VoiceSettingsResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;

import com.centralserver.demo.domain.settings.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    // 🔵 1) 보이스 설정 조회
    @GetMapping("/voice")
    public VoiceSettingsResponseDTO getVoiceSettings() {
        return settingsService.getVoiceSettings();
    }

    // 🟣 2) 보이스 설정 수정
    @PutMapping("/voice")
    public VoiceSettingsResponseDTO updateVoiceSettings(
            @RequestBody VoiceSettingsRequestDTO dto
    ) {
        return settingsService.updateVoiceSettings(dto);
    }

    // 🔵 3) 타이머 설정 조회
    @GetMapping("/timer")
    public TimerSettingsResponseDTO getTimerSettings() {
        return settingsService.getTimerSettings();
    }

    // 🟣 4) 타이머 설정 수정
    @PutMapping("/timer")
    public TimerSettingsResponseDTO updateTimerSettings(
            @RequestBody TimerSettingsRequestDTO dto
    ) {
        return settingsService.updateTimerSettings(dto);
    }

    // 🔵 5) 디테일 설정 조회
    @GetMapping("/detail")
    public DetailSettingsResponseDTO getDetailSettings() {
        return settingsService.getDetailSettings();
    }

    // 🟣 6) 디테일 설정 수정
    @PutMapping("/detail")
    public DetailSettingsResponseDTO updateDetailSettings(
            @RequestBody DetailSettingsRequestDTO dto
    ) {
        return settingsService.updateDetailSettings(dto);
    }
}