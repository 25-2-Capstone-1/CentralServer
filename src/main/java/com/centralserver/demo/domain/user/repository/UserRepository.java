package com.centralserver.demo.domain.user.repository;

import com.centralserver.demo.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Boolean existsByUserEmail(String userEmail);
    Optional<UserEntity> findByUserEmailAndIsLock(String userEmail, Boolean isLock);

    @Transactional
    void deleteByUserEmail(String userEmail);
}
