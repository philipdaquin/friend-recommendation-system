package com.example.friend_service.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.repository.FriendRepository;
import com.example.friend_service.service.FriendProducerService;
import com.example.friend_service.service.FriendService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class FriendControllerUnitTests {

    @Mock
    private FriendService service;

    @Mock   
    private FriendRepository repository;

    @Mock
    private FriendProducerService producer;

    private FriendController controller;

    @Autowired
    private WebTestClient webClient;

    private String API_VERSION = "/api/v1";

    @BeforeEach
    public void setup() { 
        controller = new FriendController(service, repository, producer);
    }

    @Test
    public void shouldGetEntityReturnsEntity() { 
        Long id = 123L;
        
        Friend friend = new Friend(1L, 2L);
        when(service.getOne(id.toString())).thenReturn(Mono.just(friend));

        // webClient.get()
        //     .uri(API_VERSION + "/friends/{id}", id)
        //     .accept(MediaType.APPLICATION_JSON)
        //     .exchange()
        //     .expectStatus().isOk()
        //     .expectBody(Friend.class)
        //     .isEqualTo(friend);

        Mono<Friend> response = controller.getEntity(id.toString());
        assertEquals(friend, response.block());
    }

    @Test
    void shouldGetUserFriendsReturnsStream() {

        var friendOne = new Friend(1L, 2L);
        var friendTwo = new Friend(1L, 3L);

        when(service.getAllByUserId(anyLong())).thenReturn(Flux.just(friendOne, friendTwo));
        
        Flux<Friend> list = controller.getUserFriends(1L);
        
        StepVerifier.create(list)
            .expectNextMatches(result -> { 
                assertNotNull(list);
                assertEquals(Long.valueOf(1), result.getUserId());
                    
                return true;
            })
            .expectComplete();
        
        var friendList = list.collectList().block();
        assertEquals(friendList.size(), 2);
    }
    
    @Test
    void shouldRemoveFriendReturnsFriendEntity() {
        Long userId = 1L, friendId = 2L;
        Friend friend = new Friend(userId, friendId);
        
        when(repository.getFriend(userId, friendId)).thenReturn(Mono.just(friend));

        when(producer.removeFriendCallback(any(Friend.class))).thenReturn(Mono.just(friend));

        Mono<Friend> result = controller.removeFriend(userId, friendId);
       
        
        StepVerifier.create(result)
            .expectNextMatches(resp -> { 
                assertNotNull(resp);
                assertEquals(resp.getUserId(), userId);
                assertEquals(resp.getFriendId(), friendId);
                return true;
            })
            .verifyComplete();

    }
    
    @Test
    void shouldAddFriendReturnsFriendEntity() {
        Long userId = 10L, friendId = 199L;
        Friend friend = new Friend(userId, friendId);
        
        when(repository.getFriend(userId, friendId)).thenReturn(Mono.just(friend));

        when(producer.addFriendCallback(any(Friend.class))).thenReturn(Mono.just(friend));
        
        Mono<Friend> result = controller.addFriend(userId, friendId);
       
        
        StepVerifier.create(result)
            .expectNextMatches(resp -> { 
                assertNotNull(resp);
                assertEquals(resp.getUserId(), userId);
                assertEquals(resp.getFriendId(), friendId);
                return true;
            })
            .expectComplete();
    }


}
