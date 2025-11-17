package com.centralserver.demo.domain.settings.voice.repository;

import com.centralserver.demo.domain.settings.voice.entity.VoiceSettings;
import com.centralserver.demo.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoiceSettingsRepository extends JpaRepository<VoiceSettings, Long> {

    // 유저별 조회
    Optional<VoiceSettings> findByUser(UserEntity user);

    // userId 기반 조회 (Controller에서 자주 쓰임)
    Optional<VoiceSettings> findByUserId(Long userId);
}