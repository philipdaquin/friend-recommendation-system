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
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.example.user_service.domains.User;

import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataR2dbcTest
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository repository;


    public String FIRSTNAME = "XXXXXX";
    
    public String LASTNAME = "AAAAAA";
    
    public String EMAIL = "PPPPP@PPPP";

    @AfterEach
    public void cleanUp() { 
        repository.deleteAll();
    }

    @Test
    public void User_GetOne_ReturnsUserObject() throws Exception {
        
        // Long id = new Random().nextLong();

        // User newUser = new User();
        // newUser.setId(id);
        // newUser.setFirstName(FIRSTNAME);
        // newUser.setLastName(LASTNAME);
        // newUser.setEmail(EMAIL);
        // newUser.setLastModifiedBy("test");
        // newUser.setLastModifiedDate(Instant.now());
    
        // User savedItem = repository.save(newUser).block(); 

        // assertNotNull(savedItem);
        
        // User response = repository.getById(id).block();
        // assertNotNull(response);
    }
}
