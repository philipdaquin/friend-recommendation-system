package com.example.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

// @EnableR2dbcRepositories
// @EnableWebFlux
@SpringBootApplication
public class UserServiceApplicationTest {

	public static void main(String[] args) {

		SpringApplication.run(UserServiceApplication.class, args);
	}

}
