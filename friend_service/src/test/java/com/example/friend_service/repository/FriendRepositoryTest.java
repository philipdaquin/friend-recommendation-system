package com.example.friend_service.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.friend_service.domains.Friend;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

// @DataR2dbcTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @TestPropertySource(properties = {
//         // "spring.datasource.url=r2dbc:postgresql://postgres:postgres@localhost:5432/test"
//         "spring.datasource.url=r2dbc:postgresql://postgres:postgres@localhost:5432/friend_service" 
// })

@DataR2dbcTest
@ExtendWith(SpringExtension.class)
public class FriendRepositoryTest {

    FriendRepository repository;

    // @BeforeEach
    // void setup() { 
    //     // repository.deleteAll();
    //     // repository.save(new Friend(1L, 2L));
    //     // repository.save(new Friend(2L, 3L));
    //     // repository.save(new Friend(4L, 1L));
    // }

    @Test
    public void shouldSaveFriendReturnsFriend() {
        Friend friend = new Friend(1L, 2L);
        
        Publisher<Friend> setup = repository.deleteAll().then(repository.save(friend));
        StepVerifier
            .create(setup)
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    public void shouldGetOneReturnsFriend() {
        // Arrange  Act  Assert 
        Friend friend = new Friend(2L, 3L);

        Publisher<Friend> setup = repository.deleteAll().then(repository.save(friend));

        Mono<Friend> find = repository.getFriend(2L, 3L);
        Publisher<Friend> composite = Mono.from(setup).then(find);

        StepVerifier
            .create(composite)
            .consumeNextWith(account -> { 
                assertEquals(friend.getUserId(), account.getUserId());
            })
            .verifyComplete();
    }
    
    @Test
    public void shouldDeleteOneReturnsVoid() {}
    

    @Test
    public void shouldPartialUpdateReturnsUpdatedFriend() {}
}
