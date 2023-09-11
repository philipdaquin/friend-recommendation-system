package com.example.friend_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.repository.FriendRepository;
import static org.mockito.Mockito.*;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @InjectMocks
    private FriendService service;

    @Mock
    private FriendRepository repository;

    private Friend friend;


    @BeforeEach
    public void init() { 
        friend = new Friend(1L, 2L);
        friend.setId(String.valueOf(1L));

    }

    @Test
    public void testSaveFriendReturnsSavedItem() { 

        // Stub the repository methods 
        when(repository.save(any(Friend.class))).thenReturn(Mono.just(friend));
        when(repository.findById(friend.getFriendId().toString())).thenReturn(Mono.just(friend));
    
        // Define a callback to be executed
        Consumer<Friend> callback = mock(Consumer.class);

        // Call the save method
        Mono<Friend> result = service.save(friend, callback);

        // Verify that the save and findById methods were called with the expected arguments
        verify(repository).save(friend);
        verify(repository).findById(friend.getId());

        // Verify that the callback was executed with the saved Friend object
        ArgumentCaptor<Friend> captor = ArgumentCaptor.forClass(Friend.class);
        verify(callback).accept(captor.capture());
        assertEquals(friend, captor.getValue());

        // Assert the result
        assertNotNull(result.block()); 
    }

}
