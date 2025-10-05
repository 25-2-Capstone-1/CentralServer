package com.CentralServer.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class OpenApiUpdateService {

    @Getter @Setter
    private JSONObject updatedData;

    @Setter @Getter
    private String area;

    @Setter @Getter
    private String apiKey;

    @Setter @Getter
    private String apiUrl;

    @PostConstruct
    public void init() {
        System.out.println("[INIT] OpenAPI Service initialized.");
    }

    // 1분마다 자동 갱신
    @Scheduled(fixedRate = 60000)
    public void fetchDataFromOpenApi() {
        System.out.println("[SCHEDULED TASK] 실행됨");

        if (area == null || apiKey == null || apiUrl == null) {
            System.out.println("OpenAPI parameters are not set.");
            return;
        }

        try {
            String encodedArea = URLEncoder.encode(area, StandardCharsets.UTF_8);
            String requestUrl = String.format("%s/%s/xml/citydata_ppltn_eng/1/5/%s",
                    apiUrl, apiKey, encodedArea);

            System.out.println("Request URL: " + requestUrl);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(requestUrl, String.class);

            System.out.println("Raw XML Response: " + response);

            // XML → JSON 변환
            JSONObject json = XML.toJSONObject(response);

            // Map 루트 안에 있는 SeoulRtd.citydata_ppltn 접근

            // 최종적으로 updatedData에 저장

            // Map 루트 안에 있는 SeoulRtd.citydata_ppltn 접근
            JSONObject seoulData = json
                    .getJSONObject("Map")
                    .getJSONObject("SeoulRtd.citydata_ppltn");

            // 최종적으로 updatedData에 저장
            updatedData = seoulData;
            JSONArray forecast = seoulData
                    .getJSONObject("FCST_PPLTN")
                    .getJSONArray("FCST_PPLTN");

            for (int i = 0; i < forecast.length(); i++) {
                JSONObject fcst = forecast.getJSONObject(i);
                System.out.println(fcst.getString("FCST_TIME") + " : " + fcst.getString("FCST_CONGEST_LVL"));
            }


            System.out.println("[" + java.time.LocalTime.now() + "] ✅ Data updated for " + area);
            System.out.println("Updated Data: " + updatedData.toString(2));



            System.out.println("[" + java.time.LocalTime.now() + "] ✅ Data updated for " + area);
            System.out.println("Updated Data: " + updatedData.toString(2));

            System.out.println("[" + java.time.LocalTime.now() + "] ✅ Data updated for " + area);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
