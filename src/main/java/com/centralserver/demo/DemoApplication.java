package com.centralserver.demo;

import com.centralserver.demo.domain.route.repository.RecommendedRouteRepository;
import com.centralserver.demo.domain.route.service.RouteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
public class DemoApplication {

    public static void main(String[] args) {
        System.out.println("Central Server is starting...DemoApplication.java");
        SpringApplication.run(DemoApplication.class, args);
    }
}