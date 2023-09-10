package com.example.friend_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
// import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestFriendServiceApplication {

	// @Bean
	// @ServiceConnection
	// @RestartScope
	// PostgreSQLContainer<?> postgresContainer() {
	// 	return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
	// }

	@Bean
	@ServiceConnection(name = "redis")
	@RestartScope
	GenericContainer<?> redisContainer() {
		return new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
	}
}
