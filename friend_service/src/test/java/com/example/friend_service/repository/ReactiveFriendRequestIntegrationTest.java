package com.example.friend_service.repository;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.friend_service.MongoContainers;
import com.example.friend_service.domains.friend_requests.FriendRequest;
import com.example.friend_service.domains.friend_requests.FriendRequestStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


/**
 *  Integration Test for Friend Request
 *  - Test custom queries 
 *  - Test Database connection
 */
@Testcontainers
@DataMongoTest
public class ReactiveFriendRequestIntegrationTest {
    @Container 
	private static MongoDBContainer mongoDBContainer = MongoContainers.getDefaultContainer();

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Autowired FriendRequestRepository repository;
	@Autowired ReactiveMongoOperations operations;

	@BeforeEach
	void setUp() {

		var recreateCollection = operations
            .collectionExists(FriendRequest.class) 
			.flatMap(exists -> exists ? operations.dropCollection(FriendRequest.class) : Mono.just(exists)) 
			.then(operations.createCollection(FriendRequest.class, CollectionOptions.empty() 
				.size(1024 * 1024) 
				.maxDocuments(100) 
				.capped()));

		recreateCollection
			.as(StepVerifier::create)
			.expectNextCount(1)
			.verifyComplete();

		var insertAll = operations.insertAll(
			Flux.just(
                    new FriendRequest(1L, 2L), 
					new FriendRequest(4L, 5L), 
					new FriendRequest(7L, 2L), 
					new FriendRequest(1L, 3L)
			).collectList());
		insertAll
			.as(StepVerifier::create)
			.expectNextCount(4)
			.verifyComplete();
	}


    


    @Test
    void shouldQueryFindFriendRequest() {
        Long userId = 1L, friendId = 2L;

        repository.getRequest(userId, friendId)
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();
    }
    @Test
    void shouldFindAllRequestsByUser() {
        Long userId = 2L;
        // var insertAll = operations.insertAll(
		// 	Flux.just(
        //             new FriendRequest(userId, 2L), 
		// 			new FriendRequest(userId, 5L), 
		// 			new FriendRequest(userId, 2L), 
		// 			new FriendRequest(userId, 3L)
		// 	).collectList());
        
        repository.findAllByUserId(userId)
            .as(StepVerifier::create)
            .expectNextCount(2)
            .expectComplete()
            .verify();
    }
    @Test
    void shouldFindAllRequestsForUser() {
        // Long friendId = 50L;
        //  var insertAll = operations.insertAll(
		// 	Flux.just(
        //             new FriendRequest(friendId, 2L ), 
		// 			new FriendRequest(friendId, 5L ), 
		// 			new FriendRequest(friendId, 2L ), 
		// 			new FriendRequest(friendId, 3L )
		// 	).collectList());
        
        repository.findAllForUser(2L)
            .as(StepVerifier::create)
            .expectNextCount(2)
            // .expectNextMatches(item -> { 
            //     return item.getRequestStatus() == FriendRequestStatus.PENDING;
            // }).
            .verifyComplete();
    }
    @Test
    void shouldGetRequestUsingUserAndFriend() {
        // Long friendId = 100L, userId = 20L;
        // FriendRequest request = new FriendRequest(userId, friendId);
        Long userId = 1L, friendId = 2L;

        // operations.insert(request);

        repository.getRequestWithUser(userId, friendId)
            .as(StepVerifier::create)
            .expectNextMatches(entity -> entity.getUserId().equals(userId))
            .expectComplete()
            .verify();
    }   
}
