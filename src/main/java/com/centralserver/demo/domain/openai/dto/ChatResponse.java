package com.centralserver.demo.domain.openai.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {

    private List<Choice> choices;

    @Data
    public static class Choice {
        private ChatMessage message;
    }
}