package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = { "com.example.demo" },
		exclude = { SecurityAutoConfiguration.class })public class AsyncapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncapiApplication.class, args);
	}

}
