package com.centralserver.demo.domain.settings.timer.repository;

import com.centralserver.demo.domain.settings.timer.entity.TimerSettings;
import com.centralserver.demo.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimerSettingsRepository extends JpaRepository<TimerSettings, Long> {

    // 유저별 조회
    Optional<TimerSettings> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);
}