package com.centralserver.demo.domain.user.entity;

import com.centralserver.demo.domain.user.dto.UserRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userEmail", unique = true, nullable = false, updatable = false)
    private String userEmail;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "is_lock", nullable = false)
    @Builder.Default
    private Boolean isLock = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    @Builder.Default
    private UserRoleType roleType =  UserRoleType.USER;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public void updateUser(UserRequestDTO dto) {
        this.nickname = dto.getNickname();
    }

    public void setPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
