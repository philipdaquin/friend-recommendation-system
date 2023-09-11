package com.example.user_service.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.r2dbc.R2DBCDatabaseContainer;

import com.example.user_service.R2DBCTestContainers;

@Testcontainers
@DataMongoTest
public class ReactiveUserRepositoryIntegrationTest {
    
    @Container 
	private static PostgreSQLContainer postgreSQLContainer = R2DBCTestContainers.getDefaultContainer();

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + postgreSQLContainer.getHost() + ":" 
                + postgreSQLContainer.getFirstMappedPort()+ "/" 
                + postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.r2dbc.password", () -> postgreSQLContainer.getPassword());
    }

    @Autowired
    R2dbcEntityTemplate template;

    @Autowired
    UserRepository repository;

    @BeforeEach
    public void setup() {}

}
