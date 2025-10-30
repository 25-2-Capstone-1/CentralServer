package com.centralserver.demo.domain.user.dto;

public record UserResponseDTO(
        String userEmail,
        String nickname,
        String username,
        String phoneNumber
) {}