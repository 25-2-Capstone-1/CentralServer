package com.centralserver.demo.domain.settings.timer.entity;

import com.centralserver.demo.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimerSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 러닝 카운트다운 on/off
    private boolean countdownEnabled;

    // 3초 / 6초 / 9초 중 1개 선택
    @Enumerated(EnumType.STRING)
    private CountdownType countdownType;

    // User (1:1 관계)
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}