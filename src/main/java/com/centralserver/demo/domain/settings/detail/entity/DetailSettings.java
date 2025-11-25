package com.centralserver.demo.domain.settings.detail.entity;

import com.centralserver.demo.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MALE, FEMALE 중 하나 (nullable 가능)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    // 키 (cm 기준, null 가능)
    private Integer height;

    // 몸무게 (kg 기준, null 가능)
    private Double weight;

    // User (1:1 관계)
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;
}
