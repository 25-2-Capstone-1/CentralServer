package com.centralserver.demo.domain.settings.voice.entity;

import com.centralserver.demo.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoiceSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------------------------
    // 페이스 메이커 설정
    // ------------------------
    private boolean pacemakerEnabled;     // 페이스 메이커 on/off

    private String pacemakerTargetTime;   // "X:XX"

    @Enumerated(EnumType.STRING)
    private VoiceType voiceType;          // 남성/여성

    private Integer voiceFrequencyMinutes; // 음성 빈도(분)

    // ------------------------
    // 네비게이션 설정
    // ------------------------
    private boolean navigationEnabled;    // 네비게이션 on/off

    @Enumerated(EnumType.STRING)
    private VoiceType navigationVoiceType; // 네비게이션 음성 타입

    // ------------------------
    // User (1:1 관계)
    // ------------------------
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}