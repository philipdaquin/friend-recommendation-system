package com.example.discovery_service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class DiscoveryServiceApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void contextLoads() {
		assertNotNull(context, "The application context should be null");
	}

}
