package com.example.user_service.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.H2Dialect;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.user_service.InfrastructureConfiguration;
import com.example.user_service.domains.User;

import reactor.test.StepVerifier;

@SpringBootTest(classes = InfrastructureConfiguration.class)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private DatabaseClient client;

    private User user;

    public String FIRSTNAME = "XXXXXX";
    
    public String LASTNAME = "AAAAAA";
    
    public String EMAIL = "PPPPP@PPPP";

    @AfterEach
    public void cleanUp() { 
        repository.deleteAll();
    }

    @BeforeEach 
    public void setup() { 
        List<String> statements = Arrays.asList(
            "DROP TABLE IF EXISTS users;",
            "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT PRIMARY KEY," +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "email VARCHAR(254) UNIQUE," +
                "created_date TIMESTAMP," +
                "created_by VARCHAR(50)," +
                "last_modified_by VARCHAR(50)," +
                "last_modified_date TIMESTAMP" +
            ");"
        );

		statements.forEach(it -> client.sql(it) //
				.fetch() //
				.rowsUpdated() //
				.as(StepVerifier::create) //
				.expectNextCount(1) //
				.verifyComplete());
    }



    @Test
    void User_GetOne_ReturnsUserObject() throws Exception {
        
        Long id = 1L;

        User newUser = new User();
        newUser.setId(id);
        newUser.setFirstName(FIRSTNAME);
        newUser.setLastName(LASTNAME);
        newUser.setEmail(EMAIL);
    
        User savedItem = repository.save(newUser).block(); 

        assertNotNull(savedItem);

        // repository.save(newUser)
        //     .as(StepVerifier::create)
        //     .assertNext(current -> { 
        //         assertEquals(current.getFirstName(), FIRSTNAME);
        //         assertEquals(current.getLastName(), LASTNAME);

        //     })
        //     .verifyComplete();
    }

    private void insert(User user) { 
        this.repository.save(user)
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();
    }
}
