package com.example.friend_service.repository;

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
import com.example.friend_service.domains.Friend;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Testcontainers
@DataMongoTest
public class ReactiveFriendRepositoryIntegrationTest {
    @Container 
	private static MongoDBContainer mongoDBContainer = MongoContainers.getDefaultContainer();

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Autowired FriendRepository repository;
	@Autowired ReactiveMongoOperations operations;

	@BeforeEach
	void setUp() {

		var recreateCollection = operations.collectionExists(Friend.class) //
				.flatMap(exists -> exists ? operations.dropCollection(Friend.class) : Mono.just(exists)) //
				.then(operations.createCollection(Friend.class, CollectionOptions.empty() //
						.size(1024 * 1024) //
						.maxDocuments(100) //
						.capped()));

		recreateCollection
			.as(StepVerifier::create)
			.expectNextCount(1)
			.verifyComplete();

		var insertAll = operations.insertAll(
			Flux.just(new Friend(1L, 2L), 
					new Friend(4L, 5L), 
					new Friend(7L, 2L), 
					new Friend(1L, 3L)
			).collectList());

		insertAll
			.as(StepVerifier::create)
			.expectNextCount(4)
			.verifyComplete();
	}

	@Test
	void shouldInsertAndCountData() { 
		var saveAndCount = repository
			.count()
			.doOnNext(System.out::println)
			.thenMany(repository.saveAll(Flux.just(
				new Friend(1L, 2L), 
				new Friend(2L, 6L)
			)))
			.last()
			.flatMap(item -> repository.count())
			.doOnNext(System.out::println);
		
		saveAndCount
			.as(StepVerifier::create)
			.expectNextCount(6)
			.verifyComplete();
		

	}
}
