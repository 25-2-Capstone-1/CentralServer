package com.centralserver.demo.domain.settings.service;

import com.centralserver.demo.domain.settings.timer.dto.TimerSettingsRequestDTO;
import com.centralserver.demo.domain.settings.timer.dto.TimerSettingsResponseDTO;
import com.centralserver.demo.domain.settings.timer.entity.CountdownType;
import com.centralserver.demo.domain.settings.timer.entity.TimerSettings;
import com.centralserver.demo.domain.settings.timer.repository.TimerSettingsRepository;
import com.centralserver.demo.domain.settings.voice.dto.VoiceSettingsRequestDTO;
import com.centralserver.demo.domain.settings.voice.dto.VoiceSettingsResponseDTO;
import com.centralserver.demo.domain.settings.voice.entity.VoiceSettings;
import com.centralserver.demo.domain.settings.voice.entity.VoiceType;
import com.centralserver.demo.domain.settings.voice.repository.VoiceSettingsRepository;
import com.centralserver.demo.domain.user.entity.UserEntity;
import com.centralserver.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final VoiceSettingsRepository voiceSettingsRepository;
    private final TimerSettingsRepository timerSettingsRepository;
    private final UserRepository userRepository;

    // ---------------------------------------------------
    // 🔵 현재 로그인 유저 가져오기
    // ---------------------------------------------------
    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUserEmailAndIsLock(email, false)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다: " + email));
    }

    // ---------------------------------------------------
    // ⭐ 신규 유저 생성 시 기본 세팅 생성
    // ---------------------------------------------------
    @Transactional
    public void createDefaultSettings(UserEntity user) {

        // 기본 Voice 설정
        VoiceSettings voice = VoiceSettings.builder()
                .user(user)
                .pacemakerEnabled(false)
                .pacemakerTargetTime("6:30")
                .voiceType(VoiceType.MALE)
                .voiceFrequencyMinutes(5)
                .navigationEnabled(true)
                .navigationVoiceType(VoiceType.MALE)
                .build();
        voiceSettingsRepository.save(voice);

        // 기본 Timer 설정
        TimerSettings timer = TimerSettings.builder()
                .user(user)
                .countdownEnabled(true)
                .countdownType(CountdownType.THREE_SECONDS)
                .build();

        timerSettingsRepository.save(timer);
    }

    // ---------------------------------------------------
    // 🔵 Voice Settings 조회
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public VoiceSettingsResponseDTO getVoiceSettings() {
        UserEntity user = getCurrentUser();
        VoiceSettings settings = voiceSettingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("보이스 설정이 없습니다."));

        return VoiceSettingsResponseDTO.builder()
                .pacemakerEnabled(settings.isPacemakerEnabled())
                .pacemakerTargetTime(settings.getPacemakerTargetTime())
                .voiceType(settings.getVoiceType())
                .voiceFrequencyMinutes(settings.getVoiceFrequencyMinutes())
                .navigationEnabled(settings.isNavigationEnabled())
                .navigationVoiceType(settings.getNavigationVoiceType())
                .build();
    }

    // ---------------------------------------------------
    // 🟣 Voice Settings 수정 (PUT)
    // ---------------------------------------------------
    @Transactional
    public VoiceSettingsResponseDTO updateVoiceSettings(VoiceSettingsRequestDTO dto) {
        UserEntity user = getCurrentUser();
        VoiceSettings settings = voiceSettingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("보이스 설정이 없습니다."));

        settings.setPacemakerEnabled(dto.getPacemakerEnabled());
        settings.setPacemakerTargetTime(dto.getPacemakerTargetTime());
        settings.setVoiceType(dto.getVoiceType());
        settings.setVoiceFrequencyMinutes(dto.getVoiceFrequencyMinutes());

        settings.setNavigationEnabled(dto.getNavigationEnabled());
        settings.setNavigationVoiceType(dto.getNavigationVoiceType());

        voiceSettingsRepository.save(settings);

        return getVoiceSettings();
    }

    // ---------------------------------------------------
    // 🔵 Timer Settings 조회
    // ---------------------------------------------------
    @Transactional(readOnly = true)
    public TimerSettingsResponseDTO getTimerSettings() {
        UserEntity user = getCurrentUser();
        TimerSettings settings = timerSettingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("타이머 설정이 없습니다."));

        return TimerSettingsResponseDTO.builder()
                .countdownEnabled(settings.isCountdownEnabled())
                .countdownType(settings.getCountdownType())
                .build();
    }

    // ---------------------------------------------------
    // 🟣 Timer Settings 수정 (PUT)
    // ---------------------------------------------------
    @Transactional
    public TimerSettingsResponseDTO updateTimerSettings(TimerSettingsRequestDTO dto) {
        UserEntity user = getCurrentUser();
        TimerSettings settings = timerSettingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("타이머 설정이 없습니다."));

        settings.setCountdownEnabled(dto.getCountdownEnabled());
        settings.setCountdownType(dto.getCountdownType());

        timerSettingsRepository.save(settings);
        return getTimerSettings();
    }

    @Transactional
    public void deleteSettingsByUser(UserEntity user) {

        // 1) Voice Settings 삭제
        voiceSettingsRepository.deleteByUser(user);

        // 2) Timer Settings 삭제
        timerSettingsRepository.deleteByUser(user);
    }
}
