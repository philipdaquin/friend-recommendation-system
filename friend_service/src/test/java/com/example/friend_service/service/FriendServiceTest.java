package com.example.friend_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.BDDMockito.given;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.external.User;
import com.example.friend_service.external.UserClient;
import com.example.friend_service.repository.FriendRepository;
import static org.mockito.Mockito.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    private FriendService service;

    @Mock
    private FriendRepository repository;

    @Mock
    private UserClient client;

    @BeforeEach
    void init() { 
        
        service = new FriendService(repository, client);
    }

    
    // Inappropriate, may need to use an external stubbing 
    
    @Test
    public void testSaveFriendReturnsSavedItemWithCallback() { 
        Friend friend = new Friend(1L, 2L);
        friend.setId(String.valueOf(1L));

        User user = new User();
        user.setId(2L);
        user.setUserId(2L);

        // Mockito should intercept this call `client.getUser()`
        when(client.getUser(friend.getFriendId())).thenReturn(Mono.just(user));
        
        when(repository.save(friend)).thenReturn(Mono.just(friend));

        when(repository.findById(friend.getId())).thenReturn(Mono.just(friend));
        
        // Define a callback to be executed
        Consumer<Friend> callback = mock(Consumer.class);
        
        // Call the save method
        Mono<Friend> result = service.save(friend, callback);
        
        // Assert the result
        StepVerifier.create(result)
            .expectNext(friend)
            .expectComplete()
            .verify();
        
        verify(callback).accept(friend);
    }

    @Test
    public void getAllByUserId_givenUserId_returns_StreamFriends() {
        Long userId = 1L;
        Friend friend = new Friend(1L, 2L);
        Friend friend2 = new Friend(2L, 3L);

        // Setup the expectationwhen the findUserId s called, the mockito should intercept that call and return
        // something we specify which is the Flux of friend1, friend2
        when(repository.findByUserId(userId)).thenReturn(Flux.just(friend, friend2));

        // Call the method 
        Flux<Friend> result = service.getAllByUserId(userId);

        // Verify that it returns the expected resutls 
        StepVerifier.create(result) 
            .expectNext(friend, friend2)
            .expectComplete()
            .verify();
    }

    @Test
    public void getOne_givenUserId_returnsFriendEntity() {
        Long userId = 1L;

        Friend friend = new Friend(1L, 2L);
        when(repository.findById(userId.toString())).thenReturn(Mono.just(friend));

        Mono<Friend> result = service.getOne(userId.toString());

        StepVerifier.create(result)
            .expectNext(friend)
            .expectComplete()
            .verify();
    }


    @Test
    public void getFriendEntity_givenUserIDAndFriend_returnsFriendEntity() {
        Long userId = 1L, friendId = 2L;

        Friend friend = new Friend(userId, friendId);

        // Intercept internal method
        when(repository.getFriend(userId, friendId)).thenReturn(Mono.just(friend));

        // Call the method 
        Mono<Friend> result = service.getFriend(userId, friendId);
        StepVerifier.create(result)
            .expectNext(friend)
            .expectComplete()
            .verify();
    }
    
    @Test
    public void deleteOne_givenUserIdAndCallback_returnsFriendEntitiy() {
        Long userId = 1L, friendId = 2L;

        Friend friend = new Friend(userId, friendId);

        // Intercept internal method
        when(repository.getFriend(userId, friendId)).thenReturn(Mono.just(friend));
        when(repository.delete(friend)).thenReturn(Mono.empty());
        
        Consumer<Friend> callback = mock(Consumer.class);

        Mono<Friend> deleted = service.deleteOne(friend, callback);
        
        StepVerifier.create(deleted)
            .expectNext(friend)
            .expectComplete()
            .verify();

        verify(callback).accept(friend);
    }
    
    @Test
    public void partialUpdate_givenNewObjects_returnsFriendEntity() {
        Consumer<Friend> callback = mock(Consumer.class);
        Friend friend = new Friend(1L, 2L);
        friend.setId(String.valueOf(1L));
        User user = new User();
            user.setId(2L);
            user.setUserId(2L);

        when(repository.findById(friend.getId())).thenReturn(Mono.just(friend));

        when(repository.save(friend)).thenReturn(Mono.just(friend));

        Mono<Friend> partial = service.partialUpdate(friend, callback);
        
        StepVerifier.create(partial)
            .expectNext(friend)
            .expectComplete()
            .verify();
        verify(callback).accept(friend);

    }
    


}
