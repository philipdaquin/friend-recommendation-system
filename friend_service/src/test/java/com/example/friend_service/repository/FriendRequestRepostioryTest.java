package com.example.friend_service.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.friend_service.domains.friend_requests.FriendRequest;

import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@DataMongoTest
public class FriendRequestRepostioryTest {
    
    @Autowired
    FriendRequestRepository repository;

    @After
    public void setup() throws Exception { 
        repository.deleteAll();
    }

    @Test
    public void shouldGetRequestReturnsEntity() { 
        Long userId = new Random().nextLong(), friendId = new Random().nextLong();
        FriendRequest request = new FriendRequest(userId, friendId);

        FriendRequest savedRequest = repository.save(request).block();
        FriendRequest result = repository.getRequest(userId, friendId).block();

        assertNotNull(result);
        assertNotNull(savedRequest);
        assertEquals(savedRequest.getUserId(), result.getUserId());
        assertEquals(savedRequest.getFriendId(), result.getFriendId());
    } 
    @Test
    void shouldFindAllRequestFromUser_ReturnsFriendRequest() { 
        Long userId = new Random().nextLong();
    
        System.out.println("USER ID =>" + userId);


        var items = repository.saveAll(Flux.just(
            new FriendRequest(userId, 1L),
            new FriendRequest(userId, 2L),
            new FriendRequest(userId, 3L),
            new FriendRequest(userId, 5L)
        ));

        Long savedUserId = items.collectList().block().get(0).getUserId();
        
        System.out.println("USER ID =>" + savedUserId);
        
        List<FriendRequest> request = repository
            .findAllByUserId(savedUserId)
            .collectList()
            .block();
        assertNotNull(request);
        assertEquals(request.size(), 4);
    }

    @Test
    void shouldFindAllRequestsForUser_ReturnsFriendRequest() {
        Long friendId = new Random().nextLong();
        var items = repository.saveAll(Flux.just(
            new FriendRequest(1L, friendId),
            new FriendRequest(2L, friendId),
            new FriendRequest(3L, friendId),
            new FriendRequest(4L, friendId)
        ));

        List<FriendRequest> saved  = items.collectList().block();
        List<FriendRequest> request = repository
            .findAllForUser(friendId)
            .collectList()
            .block();
        
        assertEquals(saved.size(), 4);
        assertNotNull(request);

    }

    @Test
    void shouldRetrieveEntityWithUserAndFriend_ReturnsEntity() { 
        Long friendId = new Random().nextLong();
        
        var items = repository.saveAll(Flux.just(
            new FriendRequest(1L, friendId),
            new FriendRequest(2L, 4L),
            new FriendRequest(3L, 1L),
            new FriendRequest(4L, 5L)
        ));

        List<FriendRequest> savedItems = items.collectList().block();

        FriendRequest item = savedItems
            .stream()
            .filter(entity -> entity.getFriendId().equals(friendId))
            .findFirst()
            .orElse(new FriendRequest());
      
        FriendRequest request = repository.getRequestWithUser(1L, friendId).block();
        assertNotNull(request);
        assertEquals(item.getId(), request.getId());

    }

    @Test
    void shouldNotExistReturnsTrue() { 
        Boolean exists = repository.existsById(String.valueOf(1)).block();
        assertFalse(exists);
    }

}
