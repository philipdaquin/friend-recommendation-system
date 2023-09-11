package com.example.friend_service.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import org.springframework.test.context.junit4.SpringRunner;

import com.example.friend_service.domains.Friend;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



// @DataR2dbcTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @TestPropertySource(properties = {
//         // "spring.datasource.url=r2dbc:postgresql://postgres:postgres@localhost:5432/test"
//         "spring.datasource.url=r2dbc:postgresql://postgres:postgres@localhost:5432/friend_service" 
// })

@RunWith(SpringRunner.class)
@DataMongoTest
public class FriendRepositoryTest {

    @Autowired
    FriendRepository repository;

    // @Autowired
    // private TestEntityManager entityManager;

    // private Friend friend1, friend2, friend3, friend4;

    // @BeforeEach
    @Before
    public void setup() { 
        repository.deleteAll();
    }

    @Test
    public void shouldSaveFriendReturnsFriend() {

        Friend savedFriend = repository.save(new Friend(1L , 10L)).block();

        assertNotNull(savedFriend);
        assertNotNull(savedFriend.getId());
    }


    @Test
    public void shouldGetFriendReturnsFriend() {
        Friend friend = new Friend(1L, 2L);
        Friend savedFriend = repository.save(friend).block();
        assertNotNull(savedFriend);
        assertNotNull(repository.findById(savedFriend.getId()));
    }

    @Test
    public void shouldDeleteFriendReturnsNull() {
        //Add an entity
        Friend friend = new Friend(1L, 2L);
        Friend savedFriend = repository.save(friend).block();

        String id = savedFriend.getId();
        // Clear
        repository.deleteById(id).block();

        // Check
        Optional<Friend> deleted = repository.findById(id).blockOptional();
        assertFalse(deleted.isPresent());
    }
    
    @Test
    public void shouldUpdateFriendReturnUpdatedFriend() {
        //Add an entity
        Friend friend = new Friend(1L, 2L);
        Friend savedFriend = repository.save(friend).block();


        Friend newFriend = new Friend(10L, 29L);
        newFriend.setId(savedFriend.getId());
        newFriend.setLastModifiedDate(Date.from(Instant.now()));

        Friend updatedValue = repository.save(newFriend).block();
        
        assertNotEquals(updatedValue.getUserId(), savedFriend.getUserId());
        assertNotEquals(updatedValue.getFriendId(), savedFriend.getFriendId());

    }
    
    @Test
    public void shouldGetAllFriendsOfUserReturnsList() {
        repository.deleteAll();
        
        repository.saveAll(Flux.just(
            new Friend(1L, 2L),
            new Friend(1L, 2L),
            new Friend(1L, 2L),
            new Friend(1L, 2L)
        ));

        Optional<List<Friend>> count = repository.findAll().collectList().blockOptional();

        assertTrue(count.isPresent());
        assertEquals(count.get().size(), 4);
    }
    
    @Test
    public void shouldGetFriendWithIdAndFriendIdReturnsFriend() {
        Long userId = new Random().nextLong(), friendId = new Random().nextLong();

        Friend newFriend = new Friend(userId, friendId);
        Friend savedFriend = repository.save(newFriend).block();

        Friend friendObject = repository
            .getFriend(savedFriend.getUserId(), savedFriend.getFriendId())
            .block();
        assertNotNull(friendObject);
    }
    
    @Test
    public void shouldGetAllRequestsByUserReturnsList() {

        Long id = new Random().nextLong();

        var items = repository.saveAll(Flux.just(
            new Friend(id, 2L),
            new Friend(id, 2L),
            new Friend(id, 2L),
            new Friend(id, 2L)
        ));

        Long savedId = items.collectList().block().get(0).getUserId();

        List<Friend> results = repository.findByUserId(savedId)
            .collectList()
            .block();
        
        assertNotNull(results);
        assertEquals(results.size(), 4);
    }
}
