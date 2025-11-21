package com.centralserver.demo.domain.googlemap.repository;

import com.centralserver.demo.domain.googlemap.entity.StreetViewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreetViewImageRepository extends JpaRepository<StreetViewImage, Long> {}