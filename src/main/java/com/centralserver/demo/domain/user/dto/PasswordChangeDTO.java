package com.centralserver.demo.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeDTO {

    // 현재 비밀번호 (본인 인증용)
    private String currentPassword;

    // 새 비밀번호
    private String newPassword;
}