package com.centralserver.demo.domain.record.repository;

import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunRecordRepository extends JpaRepository<RunRecordEntity, Long> {
    List<RunRecordEntity> findByUserId(Long userId);
    List<RunRecordEntity> findByUserIdAndBookmarkTrue(Long userId);
}