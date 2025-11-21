package com.centralserver.demo.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String role;   // "user", "assistant"
    private String content;
}