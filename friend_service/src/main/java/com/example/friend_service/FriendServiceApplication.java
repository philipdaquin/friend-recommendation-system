package com.example.friend_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

// @EnableR2dbcRepositories
// @EnableWebFlux
@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info =
	@Info(title = "Friend Service API", version = "1.0", description = "Documentation Friend Service API v1.0")
)
public class FriendServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendServiceApplication.class, args);
	}

}
