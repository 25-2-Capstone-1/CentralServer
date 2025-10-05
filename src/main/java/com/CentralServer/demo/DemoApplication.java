package com.CentralServer.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
