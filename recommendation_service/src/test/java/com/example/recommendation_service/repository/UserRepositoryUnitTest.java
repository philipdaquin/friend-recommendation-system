package com.example.recommendation_service.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.recommendation_service.friend_service.service.FriendConsumerService;
import com.example.recommendation_service.user_service.domains.User;
import com.example.recommendation_service.user_service.ranked.RankedUser;

import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *     Neo4J - Test Harness 
 *     - Provides the APIs to start and stop an embedded Neo4j server 
 */
@Transactional(propagation = Propagation.NEVER) // reactive is not supported
@DataNeo4jTest
public class UserRepositoryUnitTest {
    
    private static final Logger log = LoggerFactory.getLogger(FriendConsumerService.class);

    @Autowired
    UserRepository repository;

    private static Neo4j client;

    @BeforeAll
    static void setup() { 
        client = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer()
            .build();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.neo4j.uri", client::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
    }

    @AfterAll
    static void stopNeo4j() {
        client.close();
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void shouldInsertAndQuery() { 
        Date newDate = Date.from(Instant.now());
        User user = new User()
            .email("john.doe@example.com")
            .userId(123L)
            .firstName("John")
            .lastName("Doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);
        User savedUser = repository.save(user).single().block();

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(savedUser.getEmail(), user.getEmail());

        // log.info(savedUser.getEmail());
        // repository.findById(savedUser.getId())
        //     .as(StepVerifier::create)
        //     .consumeNextWith(item -> {
        //         assertEquals(item.getFirstName(), user.getFirstName());
        //         assertNotNull(item.getId());
        //         assertEquals(item.getEmail(), user.getEmail());
        //     })
        //     .expectComplete();
    }

    @Test
    void shouldFindUserById_GivenUserID_ReturnsUserEntity() {
        Date newDate = Date.from(Instant.now());
        User user = new User()
            .email("john.doe@example.com")
            .userId(513L)
            .firstName("John")
            .lastName("Doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        User savedUser = repository.save(user).single().block();
        assertNotNull(savedUser);
        
        User savedUserId = repository.findUserByUserId(user.getUserId()).block();
        
        assertNotNull(savedUserId);
        assertEquals(savedUserId.getCreatedBy(), savedUser.getCreatedBy());
        assertEquals(savedUserId.getEmail(), savedUser.getEmail());
        assertEquals(savedUserId.getCreatedBy(), savedUser.getCreatedBy());
        assertEquals(savedUserId.getFirstName(), savedUser.getFirstName());
        assertEquals(savedUserId.getLastName(), savedUser.getLastName());
    }

    @Test
    void shouldAddFriend_GivenUserAndFriend_ReturnsTrue() {
        Date newDate = Date.from(Instant.now());
        User user = new User()
            .email("john.doe@example.com")
            .userId(124L)
            .firstName("John")
            .lastName("Doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        User savedUser = repository.save(user).single().block();
        assertNotNull(savedUser);

        User friend = new User()
            .email("mark.doe@example.com")
            .userId(541L)
            .firstName("Mark")
            .lastName("Johns")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        User savedfriend = repository.save(friend).single().block();
        assertNotNull(savedfriend);
        
        boolean addedrelation = repository.addFriend(user.getUserId(), friend.getUserId(), newDate, newDate).block();
        assertTrue(addedrelation);
    }
    @Test
    void shouldCheckIfANonRelationshipExist_GivenUser_ReturnsFalse() {
        Date newDate = Date.from(Instant.now());
        User user = new User()
            .email("john.doe@example.com")
            .userId(123L)
            .firstName("John")
            .lastName("Doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);
        User savedUser = repository.save(user).single().block();
        assertNotNull(savedUser);

        boolean exists = repository.findRelationExist(user.getUserId(), 2L).single().block();
        assertFalse(exists);
    }

    @Test
    void shouldRemoveFriend_GivenUserAndFriend_ReturnsNothing() {
        Date newDate = Date.from(Instant.now());
        User user = new User()
            .email("john.doe@example.com")
            .userId(124L)
            .firstName("John")
            .lastName("Doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        User savedUser = repository.save(user).single().block();
        assertNotNull(savedUser);

        User friend = new User()
            .email("mark.doe@example.com")
            .userId(541L)
            .firstName("Mark")
            .lastName("Johns")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        User savedfriend = repository.save(friend).single().block();
        assertNotNull(savedfriend);
        
        // Add relation
        boolean addedrelation = repository.addFriend(user.getUserId(), friend.getUserId(), newDate, newDate).block();
        assertTrue(addedrelation);
        // Check if the relation exists 
        boolean existing = repository.findRelationExist(user.getUserId(), friend.getUserId()).block();
        assertTrue(existing);
        // Check if deleted 
        boolean deleted = repository.removeFriend(savedUser.getUserId(), savedfriend.getUserId()).block();
        assertFalse(deleted);   
        // Check if exists 
        boolean checkExists = repository.findRelationExist(user.getUserId(), friend.getUserId()).block();
        assertFalse(checkExists);
    }

    @Test
    void shouldFindMutualFriends_GivenUserAndFriendIds_ReturnsListofUsers() {
        Date newDate = Date.from(Instant.now());

        User userA = userA(); 
        User userB = userB();
        User userC = userC();
        User userD = userD();

        assertNotNull(userA);
        assertNotNull(userB);
        assertNotNull(userC);
        assertNotNull(userD);
            

        // User A to B Friendship 
        boolean friendShipAtoB = repository.addFriend(userA.getUserId(), userB.getUserId(), newDate, newDate).block();
        assertTrue(friendShipAtoB);
        boolean friendShipAtoD = repository.addFriend(userA.getUserId(), userD.getUserId(), newDate, newDate).block();
        assertTrue(friendShipAtoD);
        // User C to B friendship
        boolean friendShipCtoB = repository.addFriend(userC.getUserId(), userB.getUserId(), newDate, newDate).block();
        assertTrue(friendShipCtoB);
        boolean friendShipCtoD = repository.addFriend(userC.getUserId(), userD.getUserId(), newDate, newDate).block();
        assertTrue(friendShipCtoD);

        // Returns 2 users 
        int userAFriends = repository.findAllByUserId(userA.getUserId()).block();
        int userCFriends = repository.findAllByUserId(userC.getUserId()).block();
        
        assertEquals(userAFriends, userCFriends);
        log.info("A FRIENDS  " + userAFriends + "    " + userCFriends );

        // Find Mutual Friends between A and C, should return B
        List<User> mutuals = repository.findMutualFriends(userC.getUserId(), userA.getUserId()).collectList().block();
        assertNotNull(mutuals);
        assertEquals(mutuals.size(), 1);
    }

    @Test
    void shouldRecommendFriends_GivenUserId_ReturnsAStreamOfUsers() {
        Date newDate = Date.from(Instant.now());

        User userA = userA(); 
        User userB = userB();
        User userC = userC();
        User userD = userD();

        assertNotNull(userA);
        assertNotNull(userB);
        assertNotNull(userC);
        assertNotNull(userD);
            

        // User A to B Friendship 
        boolean friendShipAtoB = repository.addFriend(userA.getUserId(), userB.getUserId(), newDate, newDate).block();
        assertTrue(friendShipAtoB);

        // A to D 
        boolean friendShipAtoD = repository.addFriend(userA.getUserId(), userD.getUserId(), newDate, newDate).block();
        assertTrue(friendShipAtoD);
        
        // B to C
        boolean friendShipBtoC = repository.addFriend(userB.getUserId(), userC.getUserId(), newDate, newDate).block();
        assertTrue(friendShipBtoC);
            
        List<RankedUser> recommendedFriends = repository.recommendFriends(userA.getUserId(), RankedUser.class).collectList().block();
        
        assertNotNull(recommendedFriends);
        assertEquals(recommendedFriends.size(), 1);
    }


    User userA() { 
        Date newDate = Date.from(Instant.now());

        User mark = new User()
            .email("john.doe@example.com")
            .userId(124L)
            .firstName("John")
            .lastName("Doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        return repository.save(mark).single().block();
    }

    User userB() {
        Date newDate = Date.from(Instant.now());

        User john = new User()
            .email("mark.doe@example.com")
            .userId(541L)
            .firstName("Mark")
            .lastName("Johns")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        return repository.save(john).single().block();
    }
    User userC() {
        Date newDate = Date.from(Instant.now());
        User susan = new User()
            .email("susan.doe@example.com")
            .userId(24L)
            .firstName("susan")
            .lastName("Doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        return repository.save(susan).single().block();
    }
    User userD() {
        Date newDate = Date.from(Instant.now());

        User friendB = new User()
            .email("jane.doe@example.com")
            .userId(54L)
            .firstName("jane")
            .lastName("doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        return repository.save(friendB).single().block();
    }

}
