package com.centralserver.demo.domain.record.repository;

import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RunRecordRepository extends JpaRepository<RunRecordEntity, Long> {

    /** -------------------------
     *  기본 조회 메서드
     *  ------------------------- */

    // 특정 유저의 전체 기록
    List<RunRecordEntity> findAllByUser_Id(Long userId);

    // 북마크한 기록만 조회
    List<RunRecordEntity> findAllByUser_IdAndBookmarkTrue(Long userId);


    // 특정 날짜 범위 조회
    List<RunRecordEntity> findByUser_IdAndStartTimeBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

}