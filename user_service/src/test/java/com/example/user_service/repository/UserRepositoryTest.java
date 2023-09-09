package com.example.user_service.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.H2Dialect;
import org.springframework.r2dbc.core.DatabaseClient;

import com.example.user_service.domains.User;
import com.example.user_service.errors.UserResourceException;
import com.example.user_service.service.KafkaProducerService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private DatabaseClient databaseClient;

    private User user;

    public String FIRSTNAME = "XXXXXX";
    
    public String LASTNAME = "AAAAAA";
    
    public String EMAIL = "PPPPP@PPPP";

    @BeforeEach 
    public void setup() { 
        user.setFirstName(FIRSTNAME);
        user.setLastName(LASTNAME);
        user.setEmail(EMAIL);

        manager.persist(user);
        manager.flush();
    }

    @Test
    void User_GetOne_ReturnsUserObject() {
        
        User newUser = new User();
        newUser.setId(1L);
        newUser.setFirstName(FIRSTNAME);
        newUser.setLastName(LASTNAME);
        newUser.setEmail(EMAIL);

        R2dbcEntityTemplate template = new R2dbcEntityTemplate(databaseClient, H2Dialect.INSTANCE);
        template.insert(User.class)
            .using(newUser)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();

        Mono<User> findUser = repository.findById(1L);
        
        findUser.as(StepVerifier::create)
            .assertNext(current -> { 
                assertEquals(current.getFirstName(), FIRSTNAME);
                assertEquals(current.getLastName(), LASTNAME);
            })
            .verifyComplete();

    }

}
