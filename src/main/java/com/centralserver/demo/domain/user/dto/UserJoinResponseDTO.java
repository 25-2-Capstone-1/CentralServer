package com.centralserver.demo.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserJoinResponseDTO {
    private Long userId;
    private String username;
}