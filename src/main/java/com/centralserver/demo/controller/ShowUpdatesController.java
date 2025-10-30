package com.centralserver.demo.controller;

import com.centralserver.demo.service.OpenApiUpdateService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShowUpdatesController {

    private final OpenApiUpdateService service;

    public ShowUpdatesController(OpenApiUpdateService service) {
        this.service = service;
    }

    // ✅ API 기본 정보 설정 (초기 1회 호출)
    @PostMapping("/config")
    public String setApiConfig(@RequestParam String apiKey,
                               @RequestParam String apiUrl) {
        service.setApiKey(apiKey);
        service.setApiUrl(apiUrl);
        return "✅ OpenAPI parameters configured successfully.";
    }

    // ✅ 모든 지역의 최신 데이터 반환
    @GetMapping(value = "/population/all", produces = "application/json; charset=UTF-8")
    public Map<String, JSONObject> getAllData() {
        Map<String, JSONObject> data = service.getAllUpdatedData();
        if (data == null || data.isEmpty()) {
            return Map.of("message", new JSONObject().put("info", "데이터가 아직 없습니다. 잠시 후 다시 시도하세요."));
        }
        return data;
    }

    // ✅ 특정 지역만 보기
    @GetMapping(value = "/population/{area}", produces = "application/json; charset=UTF-8")
    public String getSingleArea(@PathVariable String area) {
        JSONObject data = service.getAllUpdatedData().get(area);
        if (data != null) {
            return data.toString(4);
        } else {
            return "{ \"message\": \"해당 지역의 데이터가 없습니다.\" }";
        }
    }
}
