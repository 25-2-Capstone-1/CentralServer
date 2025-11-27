package com.centralserver.demo.domain.settings.service;

import com.centralserver.demo.domain.settings.detail.dto.DetailSettingsRequestDTO;
import com.centralserver.demo.domain.settings.detail.dto.DetailSettingsResponseDTO;
import com.centralserver.demo.domain.settings.detail.entity.DetailSettings;
import com.centralserver.demo.domain.settings.detail.repository.DetailSettingsRepository;
import com.centralserver.demo.domain.settings.pace.dto.PaceRecommendationResponseDTO;
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
import com.centralserver.demo.util.PaceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final VoiceSettingsRepository voiceSettingsRepository;
    private final TimerSettingsRepository timerSettingsRepository;
    private final DetailSettingsRepository detailSettingsRepository;
    private final UserRepository userRepository;

    private final PaceCalculator paceCalculator;

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

        // 기본 Detail Settings
        DetailSettings detail = DetailSettings.builder()
                .user(user)
                .gender(null)     // 아직 입력 안 한 상태
                .height(null)     // 기본값 없음
                .weight(null)     // 기본값 없음
                .build();

        detailSettingsRepository.save(detail);
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

    @Transactional(readOnly = true)
    public DetailSettingsResponseDTO getDetailSettings() {
        UserEntity user = getCurrentUser();
        DetailSettings settings = detailSettingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("상세 설정이 없습니다."));

        return DetailSettingsResponseDTO.builder()
                .gender(settings.getGender())
                .height(settings.getHeight())
                .weight(settings.getWeight())
                .build();
    }

    @Transactional
    public DetailSettingsResponseDTO updateDetailSettings(DetailSettingsRequestDTO dto) {
        UserEntity user = getCurrentUser();
        DetailSettings settings = detailSettingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("상세 설정이 없습니다."));

        settings.setGender(dto.getGender());
        settings.setHeight(dto.getHeight());
        settings.setWeight(dto.getWeight());

        detailSettingsRepository.save(settings);

        return getDetailSettings();
    }

    // 첫 로그인 판별
    public boolean isFirstLogin(UserEntity user) {

        Optional<DetailSettings> detailOpt = detailSettingsRepository.findByUser(user);

        // 🔵 레코드 자체가 없으면 → 무조건 첫 로그인
        if (detailOpt.isEmpty()) {
            return true;
        }

        DetailSettings detail = detailOpt.get();

        // 🔵 레코드는 있으나 값이 모두 null이면 첫 로그인
        return detail.getGender() == null &&
                detail.getHeight() == null &&
                detail.getWeight() == null;
    }

    @Transactional
    public void deleteSettingsByUser(UserEntity user) {

        // 1) Voice Settings 삭제
        voiceSettingsRepository.deleteByUser(user);

        // 2) Timer Settings 삭제
        timerSettingsRepository.deleteByUser(user);

        // 3) Detail Settings 삭제
        detailSettingsRepository.deleteByUser(user);
    }

    // 사용자의 세션 정보를 바탕으로 난이도별 pace 추천
    public PaceRecommendationResponseDTO getPaceRecommendation() {
        UserEntity user = getCurrentUser();  // JWT 기반 사용자 조회 로직

        DetailSettings settings = detailSettingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("상세 설정이 없습니다."));

        DetailSettingsResponseDTO userDetail = DetailSettingsResponseDTO.builder()
                .gender(settings.getGender())
                .height(settings.getHeight())
                .weight(settings.getWeight())
                .build();

        return paceCalculator.calculatePace(userDetail);
    }
}
