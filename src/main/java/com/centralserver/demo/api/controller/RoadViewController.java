package com.centralserver.demo.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoadViewController {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @GetMapping(value = "/roadview", produces = "text/html; charset=UTF-8")
    public String roadview(@RequestParam double lat, @RequestParam double lng, Model model) {
        model.addAttribute("lat", lat);
        model.addAttribute("lng", lng);
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        return "roadview";
    }
}