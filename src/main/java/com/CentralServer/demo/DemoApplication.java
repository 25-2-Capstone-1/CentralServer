package com.CentralServer.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Central Server is starting...");
		SpringApplication.run(DemoApplication.class, args);
	}

}
