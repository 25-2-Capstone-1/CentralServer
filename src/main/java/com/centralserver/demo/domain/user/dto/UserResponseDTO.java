package com.centralserver.demo.domain.user.dto;

public record UserResponseDTO(
        Long userId,
        String userEmail,
        String nickname,
        String username,
        String phoneNumber
) {}