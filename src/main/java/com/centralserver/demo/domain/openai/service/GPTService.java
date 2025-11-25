package com.centralserver.demo.domain.openai.service;

import com.centralserver.demo.domain.openai.dto.ChatMessage;
import com.centralserver.demo.domain.openai.dto.ChatRequest;
import com.centralserver.demo.domain.openai.dto.ChatResponse;
import com.centralserver.demo.domain.openai.dto.RouteEnhanceResult;
import com.centralserver.demo.domain.route.dto.RecommendedRawRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GPTService {

    private final WebClient openAIWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.model}")
    private String openaiModel;

    /**
     * GPT를 호출하여 러닝 코스의:
     * - difficulty
     * - routeName
     * - description
     * 을 생성한다.
     */
    public RouteEnhanceResult callGptForEnhancement(RecommendedRawRouteDTO raw, RouteRequestDTO req) {

        // ---- 1. Prompt 생성 ----
        String prompt = """
            You are a running route recommendation AI that generates metadata for a running course.

            Based on the input below, generate the following fields:
            1. difficulty     (easy / medium / hard)
            2. routeName      (use region name inferred from coordinates, not numbers)
            3. description    (short Korean description)

            ### INPUT ###
            user_location: (%f, %f)
            start_point: (%f, %f)
            end_point: (%f, %f)
            distance_meters: %.2f
            slope_preference: %d
            traffic_light_preference: %d
            congestion_preference: %d

            ### RULES ###
            - routeName:
                * Must be Korean region-based name inferred from location.
                * NO numeric coordinates.
                * Examples: "한강 북단 러닝 코스", "성수동 야경 코스", "도심 하천 산책 코스"
                * Add distance in km at the end: e.g., "한강 동측 러닝 코스 5.3km"

            - difficulty:
                * Use combination of distance + slope + trafficLights + congestion.

            - description:
                * 1~2 sentences
                * Korean
                * Friendly and descriptive.

            ### OUTPUT FORMAT (VERY IMPORTANT) ###
            - Return ONLY this exact JSON object:
            {
              "difficulty": "",
              "routeName": "",
              "description": ""
            }

            - NEVER wrap the JSON in code block.
            - NEVER include ``` or ```json.
            - The response MUST be pure JSON.
            """.formatted(
                req.myPoint().lat(),
                req.myPoint().lng(),
                raw.getStartPoint().lat(),
                raw.getStartPoint().lng(),
                raw.getEndPoint().lat(),
                raw.getEndPoint().lng(),
                raw.getDistance(),        //🔥 수정됨
                req.slope(),
                req.trafficLights(),
                req.trafficCongestion()
        );

        // ---- 2. API Request ----
        ChatRequest request = new ChatRequest(
                openaiModel,
                List.of(new ChatMessage("user", prompt))
        );

        ChatResponse response = openAIWebClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        if (response == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("❌ OpenAI returned empty response");
        }

        String content = response.getChoices().getFirst().getMessage().getContent();

        // ---- 3. GPT가 코드블록을 붙여도 제거하는 방어 코드 ----
        content = cleanup(content);

        // ---- 4. JSON 파싱 ----
        try {
            return objectMapper.readValue(content, RouteEnhanceResult.class);
        } catch (Exception e) {
            throw new RuntimeException("❌ GPT JSON 파싱 실패: " + content, e);
        }
    }

    /**
     * GPT 응답에 포함될 수 있는 코드블록 제거
     */
    private String cleanup(String content) {
        return content
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}
