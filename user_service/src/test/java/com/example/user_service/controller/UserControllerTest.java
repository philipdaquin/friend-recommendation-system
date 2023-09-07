package com.example.user_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.AbstractIntegrationTest;
import com.example.user_service.domains.User;
import com.example.user_service.service.UserService;

import reactor.core.publisher.Mono;

// @RunWith(SpringRunner.class)
// @WebFluxTest(UserController.class)
public class UserControllerTest extends AbstractIntegrationTest {
    

    private static final String FIRST_NAME = "AAAAAAA";
    
    private static final String LAST_NAME = "BBBBBBB";

    private static final String EMAIL = "AAAAA@AAAAA.com";
    
    private static final Instant CREATED_DATE = Instant.now();
    
    private static final String CREATED_BY = "admin";

    private static final String ENTITY_API_URI = "/api/v1/users";

    @Autowired
    private WebTestClient client;

    @MockBean
    private UserService service;

    @MockBean
    private KafkaProducer producer;

    @Test
    public void testCreateUserDetails_returnsUser() throws Exception { 
        
        // Define a sample User
        User user = new User(); 
        user.setId(null);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        user.setCreatedBy(CREATED_BY);
        user.setCreatedDate(CREATED_DATE);

        // Define a sample reponse from User Service 
        when(service.save(any(User.class), any())).thenReturn(Mono.just(user));

        // Perform a POST request to the /user endpoint
        client.post()
            .uri(ENTITY_API_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(user), User.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(User.class)
            .isEqualTo(user);

    }

}
