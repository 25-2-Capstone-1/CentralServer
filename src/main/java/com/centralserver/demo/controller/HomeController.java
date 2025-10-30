package com.centralserver.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    //@GetMapping("/")
    public String home() {
        return "home_start_page";
        // This will render the home.html template
    }
}