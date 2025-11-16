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
    List<RunRecordEntity> findByUser_IdAndBookmarkTrue(Long userId);

//    // 제목 부분 검색 (예: "한강" 포함)
//    List<RunRecordEntity> findByUser_IdAndTitleContaining(Long userId, String keyword);

    // 특정 날짜 이후 기록 조회
    List<RunRecordEntity> findByUser_IdAndStartTimeAfter(Long userId, LocalDateTime date);

    // 특정 날짜 범위 조회
    List<RunRecordEntity> findByUser_IdAndStartTimeBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    /** -------------------------
     *  정렬 기반 조회
     *  ------------------------- */

//    // 최신 기록 순
//    List<RunRecordEntity> findByUser_IdOrderByStartTimeDesc(Long userId);
//
//    // 오래된 등록순
//    List<RunRecordEntity> findByUser_IdOrderByStartTimeAsc(Long userId);
//
//    // 거리 기준 내림차순
//    List<RunRecordEntity> findByUser_IdOrderByDistanceKmDesc(Long userId);
//
//    // 페이스 느림/빠름 검색도 원하면 가능
//    List<RunRecordEntity> findByUser_IdOrderByDurationSecondsDesc(Long userId);


    /** -------------------------
     *  필터링 + 정렬 조합
     *  ------------------------- */

    // 북마크 기록 + 최신순
    List<RunRecordEntity> findByUser_IdAndBookmarkTrueOrderByStartTimeDesc(Long userId);
//
//    // 특정 난이도별 조회: EASY / MEDIUM / HARD
//    List<RunRecordEntity> findByUser_IdAndDifficulty(Long userId, String difficulty);
//
//    // 난이도별 + 최신순
//    List<RunRecordEntity> findByUser_IdAndDifficultyOrderByStartTimeDesc(
//            Long userId,
//            String difficulty
//    );
}