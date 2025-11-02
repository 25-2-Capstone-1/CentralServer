package com.centralserver.demo.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserUpdateResponseDTO {
    private Long userId;
    private String nickname;
    private LocalDateTime updatedDate;
}
