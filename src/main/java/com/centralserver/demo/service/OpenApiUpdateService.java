package com.centralserver.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class OpenApiUpdateService {

    @Getter @Setter
    private Map<String, JSONObject> allUpdatedData = new HashMap<>();
    @Setter @Getter
    private String area;
    @Setter @Getter
    private String apiKey;

    @Setter @Getter
    private String apiUrl;

    private List<String> areaList = new ArrayList<>();

    @PostConstruct
    public void init() {
        System.out.println("[INIT] OpenAPI Service initialized.");

        try {
            // ✅ resources/areaCode.txt 읽기
            ClassPathResource resource = new ClassPathResource("areaCode.txt");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    int blankIndex = line.indexOf(' ');
                    String result = (blankIndex != -1) ? line.substring(0, blankIndex) : line;
                    //앞의 지역 코드만 따로 분리
                    areaList.add(result); // area code ex: POI033만 사용
                    // 한글 이름을 활용하면 로컬에서는 잘 되지만 서버에서는 인코딩 이슈로 깨짐...
                }
            }
            reader.close();
            System.out.println("✅ Loaded " + areaList.size() + " areas from areaCode.txt");
        } catch (Exception e) {
            System.out.println("❌ Failed to load areaCode.txt: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 600000)
    public void fetchDataFromOpenApi() {
        if (apiKey == null || apiUrl == null) {
            System.out.println("OpenAPI parameters are not set.");
            return;
        }

        if (areaList.isEmpty()) {
            System.out.println("❌ No area list loaded.");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        Map<String, JSONObject> tempData = new HashMap<>();

        for (String area : areaList) {
            try {
                String encodedArea = URLEncoder.encode(area, StandardCharsets.UTF_8);
                String requestUrl = String.format("%s/%s/xml/citydata_ppltn_eng/1/5/%s",
                        apiUrl, apiKey, encodedArea);

                String response = restTemplate.getForObject(requestUrl, String.class);
                JSONObject json = XML.toJSONObject(response);
                JSONObject seoulData = json
                        .getJSONObject("Map")
                        .getJSONObject("SeoulRtd.citydata_ppltn");

                tempData.put(area, seoulData);

                System.out.println("✅ " + area + " 데이터 갱신 완료");
                Thread.sleep(300); // rate limit 방지

            } catch (Exception e) {
                System.out.println("❌ " + area + " Error: " + e.getMessage());
            }
        }

        allUpdatedData = tempData;
        System.out.println("[" + java.time.LocalTime.now() + "] ✅ 전체 지역 데이터 갱신 완료 (" + tempData.size() + "개)");
    }
    //test github push
}
