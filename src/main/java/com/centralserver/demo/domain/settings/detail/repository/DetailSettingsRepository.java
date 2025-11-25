package com.centralserver.demo.domain.settings.detail.repository;

import com.centralserver.demo.domain.settings.detail.entity.DetailSettings;
import com.centralserver.demo.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailSettingsRepository extends JpaRepository<DetailSettings, Long> {
    Optional<DetailSettings> findByUser(UserEntity user);
    void deleteByUser(UserEntity user);
}