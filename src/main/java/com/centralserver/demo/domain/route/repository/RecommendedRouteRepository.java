package com.centralserver.demo.domain.route.repository;

import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecommendedRouteRepository extends JpaRepository<RecommendedRoute, Long> {

    /**
     * 1. routeId로 조회
     * - JpaRepository 기본 제공 findById() 사용 가능하지만
     *   커스텀 메서드도 종종 사용됨
     */
    Optional<RecommendedRoute> findByRouteId(Long routeId);

    /**
     * 2. 특정 날짜 이후 생성된 추천 경로 조회
     */
    List<RecommendedRoute> findByCreatedAtAfter(LocalDateTime dateTime);

    /**
     * 3. 특정 기간 안에 생성된 경로 조회
     */
    List<RecommendedRoute> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 4. routeId로 단일 삭제
     */
    void deleteByRouteId(Long routeId);

    /**
     * 5. routeId 존재 여부 체크
     */
    boolean existsByRouteId(Long routeId);
}
