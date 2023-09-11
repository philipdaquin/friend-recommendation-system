package com.example.friend_service.repository;

import org.junit.jupiter.api.BeforeEach;
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

		// var recreateCollection = operations.collectionExists(Person.class) //
		// 		.flatMap(exists -> exists ? operations.dropCollection(Person.class) : Mono.just(exists)) //
		// 		.then(operations.createCollection(Person.class, CollectionOptions.empty() //
		// 				.size(1024 * 1024) //
		// 				.maxDocuments(100) //
		// 				.capped()));

		// recreateCollection.as(StepVerifier::create).expectNextCount(1).verifyComplete();

		// var insertAll = operations.insertAll(Flux.just(new Person("Walter", "White", 50), //
		// 				new Person("Skyler", "White", 45), //
		// 				new Person("Saul", "Goodman", 42), //
		// 		new Person("Jesse", "Pinkman", 27)).collectList());

		// insertAll.as(StepVerifier::create).expectNextCount(4).verifyComplete();
	}
}
