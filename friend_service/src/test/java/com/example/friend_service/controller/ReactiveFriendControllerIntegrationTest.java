package com.example.friend_service.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.repository.FriendRepository;
import com.example.friend_service.service.FriendProducerService;
import com.example.friend_service.service.FriendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import reactor.core.publisher.Mono;
import org.junit.jupiter.api.TestInstance;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReactiveFriendControllerIntegrationTest {
    
    @Mock
    private FriendRepository repository;

    @Mock
    private FriendProducerService friendProducer;

    @Mock
    private FriendService service; 

    @Autowired
    private MockMvc mock;

    private Friend friend;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() { 
        friend = new Friend(1L, 2L);
    }

    @Test
    public void shouldGetFriendEntityGivenFriendIDReturnsFriendEntity() throws Exception { 
        String id = String.format("1");
        when(service.getOne(id)).thenReturn(Mono.just(friend));
        
        mock.perform(get("/api/v1/friends/" + id)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .contentType(mapper.writeValueAsString(friend)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id));

    }


}
