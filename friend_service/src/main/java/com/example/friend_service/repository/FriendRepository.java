package com.example.friend_service.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import org.springframework.stereotype.Repository;

import com.example.friend_service.domains.Friend;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@SuppressWarnings("unused")
@Repository
public interface FriendRepository extends ReactiveMongoRepository<Friend, String> {

    /**
     * Gets the Friend entity using `userId` and `friendId`
     * 
     * @param userId
     * @param friendId
     * @return
     */
    @Query("{'user_id' : ?0 , 'friend_id' : ?1}")
    Mono<Friend> getFriend(Long userId, Long friendId);

    /**
     * Gets all the Friends connected to the User 
     * 
     * @param userId
     * @return
     */
    Flux<Friend> findByUserId(Long userId);
}
