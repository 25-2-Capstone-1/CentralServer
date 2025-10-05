package com.CentralServer.demo.Controller;

import com.CentralServer.demo.Service.OpenApiUpdateService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShowUpdatesController {

    private final OpenApiUpdateService service;

    public ShowUpdatesController(OpenApiUpdateService service) {
        this.service = service;
    }

    // ✅ API 정보 입력 (한 번만 호출하면 됨)
    @GetMapping("/setArea")
    public String setArea(@RequestParam String area,
                          @RequestParam String apiKey,
                          @RequestParam String apiUrl) {
        service.setArea(area);
        service.setApiKey(apiKey);
        service.setApiUrl(apiUrl);

        // 설정 즉시 한 번 데이터 불러오기
        service.fetchDataFromOpenApi();

        return "✅ OpenAPI parameters set for area: " + area;
    }

    // ✅ JSON 데이터 보기
    @GetMapping(value = "/updatedData", produces = "application/json; charset=UTF-8")
    public String showJson() {
        JSONObject data = service.getUpdatedData();
        if (data != null) {
            return data.toString(4); // 보기 좋게 포맷
        } else {
            return "{ \"message\": \"데이터가 아직 없습니다. 잠시 후 다시 시도하세요.\" }";
        }
    }
}
