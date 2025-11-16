package com.centralserver.demo.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApiResponseDTO<T> {

    private boolean success;
    private T data;
    private String error;

    // 성공 응답
    public static <T> ApiResponseDTO<T> success(T data) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .data(data)
                .error(null)
                .build();
    }

    // 실패 응답
    public static <T> ApiResponseDTO<T> error(String errorMessage) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .data(null)
                .error(errorMessage)
                .build();
    }
}