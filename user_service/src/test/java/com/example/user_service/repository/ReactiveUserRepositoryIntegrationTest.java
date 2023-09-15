package com.example.user_service.repository;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.Assert;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.r2dbc.R2DBCDatabaseContainer;

import com.example.user_service.R2DBCTestContainers;
import com.example.user_service.domains.User;

import reactor.test.StepVerifier;

@Testcontainers
@DataR2dbcTest
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
    DatabaseClient client;

    @Autowired
    UserRepository repository;

    @BeforeEach
    public void setup() {

        // // Create a new database 
        // List<String> statements = Arrays.asList(
        //     "DROP TABLE IF EXISTS users;",
        //     "CREATE TABLE IF NOT EXISTS users (" +
        //         "id BIGINT PRIMARY KEY," +
        //         "first_name VARCHAR(50)," +
        //         "last_name VARCHAR(50)," +
        //         "email VARCHAR(254) UNIQUE," +
        //         "created_date TIMESTAMP," +
        //         "created_by VARCHAR(50)," +
        //         "last_modified_by VARCHAR(50)," +
        //         "last_modified_date TIMESTAMP" +
        //     ");"
        // );

		// statements.forEach(it ->  client.sql(it) //
		// 		.fetch() //
		// 		.rowsUpdated() //
		// 		.as(StepVerifier::create) //
		// 		.expectNextCount(1) //
		// 		.verifyComplete());
    }

    @Test
    void testInsertAndQuery() {
        
        User user = new User()
            .firstName("john")
            .lastName("doe")
            .createdBy("test")
            .email("johndoe@gmail.com");
        User saved = repository.save(user).single().block();
        assertEquals(null, 0, 0, 0);
        repository.getById(saved.getId())
            .as(StepVerifier::create)
            .expectNextMatches(item -> {
                assertEquals(item.getFirstName(), user.getFirstName());
                return true;
            })
            .expectComplete()
            .verify();
    }

}
