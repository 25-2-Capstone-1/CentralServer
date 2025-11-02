package com.centralserver.demo.domain.route.repository;

import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedRouteRepository extends JpaRepository<RecommendedRoute, String> {
}
