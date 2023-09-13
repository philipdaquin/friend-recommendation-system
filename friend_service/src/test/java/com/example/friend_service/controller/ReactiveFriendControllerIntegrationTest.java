package com.example.friend_service.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.friend_service.WireMockService;
import com.example.friend_service.domains.Friend;
import com.example.friend_service.repository.FriendRepository;
import com.example.friend_service.service.FriendProducerService;
import com.example.friend_service.service.FriendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class ReactiveFriendControllerIntegrationTest extends WireMockService {
    
    @Mock
    private FriendRepository repository;

    @Mock
    private FriendProducerService producer;

    @Mock
    private FriendService service; 

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setup() { 
        // Fill in Repository with Mock Friends 
        // Random id = new Random();

        // for (int i = 0; i < 3; i ++) { 
        //     Friend friendOne = new Friend(id.nextLong(), Long.valueOf(i + 1));
            
        //     // mock.perform(
        //     //     post("/api/v1/")
        //     // )
        //     repository.save(friendOne);
        // }
    }


    @Test
    public void shouldGetFriendEntityGivenFriendIDReturnsFriendEntity() throws Exception { 
        Long id = 1L;

        Friend friend = new Friend(1L, 2L);
        friend.setId(String.valueOf(id));

        when(service.getOne(anyString())).thenReturn(Mono.just(friend));

        MvcResult response = mock.perform(get("/api/v1/friends/" + id))
            // .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn();
        
            System.out.println(friend);
        
        
        String content = response.getResponse().getContentAsString();
        Friend friendDto = mapper.readValue(content, Friend.class);
        
        assertEquals(friendDto.getUserId(), friend.getUserId());

    }
    @Test
    void getNonExistingFriendEntity() throws Exception { 
        Long id = 1L;
        
        when(repository.existsById(anyString())).thenReturn(Mono.empty());
        when(service.getOne(anyString())).thenReturn(Mono.empty());

        String url = String.format("/api/v1/friends/" + id);

        mock.perform(get(url))
            .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldAddFriendReturnsFriendEntity() throws Exception {
        Long id = 1L, userId = 2L, friendId = 3L;
        
        Friend friend = new Friend(userId, friendId);
        friend.setId(String.valueOf(id));

        when(repository.getFriend(userId, friendId)).thenReturn(Mono.just(friend));
        when(producer.addFriendCallback(friend)).thenReturn(Mono.just(friend));

        String url = String.format("/api/v1/users/%s/command/add-friend", String.valueOf(id));

        MvcResult result = mock.perform(post(url)
            .param("friendId", String.valueOf(3L))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.userId").value(userId))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        Friend friendDto = mapper.readValue(content, Friend.class);
        assertEquals(friendDto.getUserId(), friend.getUserId());

    }   
    
    @Test
    public void shouldGetAllFriendsByUserReturnsListOfFriends() throws Exception {
        Long userId = 1L;
        
        Flux<Friend> list = Flux.just(
            new Friend(userId, 2L),
            new Friend(userId, 4L),
            new Friend(userId, 6L)
        );

        when(service.getAllByUserId(userId)).thenReturn(list);

        String url = String.format("/api/v1/users/%s/friends", String.valueOf(userId));

        MvcResult result = mock.perform(get(url)
            .param("friendId", String.valueOf(3L))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.userId").value(userId))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Friend> friendDto = mapper.readValue(content, new TypeReference<List<Friend>>() {});
        
        assertEquals(friendDto.size(), 3);
        assertEquals(friendDto.get(0).getUserId(), userId);
    }
    
    @Test
    public void shouldRemoveFriendFromUserReturnsRemovedEntity() throws Exception {
        Long id = 1L, userId = 2L, friendId = 3L;
        
        Friend friend = new Friend(userId, friendId);
        friend.setId(String.valueOf(id));

        when(repository.getFriend(userId, friendId)).thenReturn(Mono.just(friend));
        when(producer.removeFriendCallback(friend)).thenReturn(Mono.just(friend));

        String url = String.format("/api/v1/users/%s/command/remove-friend", String.valueOf(id));

        MvcResult result = mock.perform(delete(url)
            .param("friendId", String.valueOf(3L))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.userId").value(userId))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        Friend friendDto = mapper.readValue(content, Friend.class);
        assertEquals(friendDto.getUserId(), friend.getUserId());
    }


}
