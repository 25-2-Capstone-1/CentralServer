package com.centralserver.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "roadview")
@Data
public class RoadviewConfig {
    private String baseUrl;
}
