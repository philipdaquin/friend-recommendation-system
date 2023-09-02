package com.example.user_service.repository;


import static org.junit.Assert.assertNotNull;

import java.time.Instant;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.user_service.domains.User;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
@SpringJUnitConfig(classes = {DatabaseClient.class, UserRepositoryTest.TestConfig.class})
public class UserRepositoryTest {
    
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    DatabaseClient client;

    @BeforeEach
    public void setup() {
        userRepository
            .deleteAll()
            .subscribe(data -> log.info("clean database: {} deleted.", data)
        );
    }


    @Test
    public void Should_Save_ReturnsSavedItem() { 
        User user = new User();
        user.setFirstName("john");
        user.setLastName("dor");
        user.setEmail("johndoe@gmail.com");
        user.setCreatedBy("admin");
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy("admin");
        user.setLastModifiedDate(Instant.now());
        
        
        userRepository.save(user)
            .as(StepVerifier::create)
            .expectNextMatches(item -> item.getId() != null)
            .verifyComplete();

    }

    @Test
    public void Should_Delete_ReturnsVoid() { 
        User user = new User();
        user.setFirstName("paul");
        user.setLastName("dore");
        user.setEmail("pauldore@gmail.com");
        user.setCreatedBy("admin");
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy("admin");
        user.setLastModifiedDate(Instant.now());

        Mono<User> delete = userRepository
            .save(user)
            .flatMap(saved -> userRepository.delete(user).thenReturn(saved));

        StepVerifier
            .create(delete)
            .expectNextMatches(item -> user.getId() != null)
            .verifyComplete();
    }

    @ComponentScan
    static class TestConfig {}
}
