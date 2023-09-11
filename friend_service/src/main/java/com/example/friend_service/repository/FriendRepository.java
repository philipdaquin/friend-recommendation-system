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
public interface FriendRepository extends ReactiveMongoRepository<Friend,Long> {

    @Query("{'user_id' : ?0}")
    Flux<Friend> findAllByUserId(final Long userId);

    @Query("{'user_id' : ?0 , 'friend_id' : ?1}")
    Mono<Friend> getFriend(Long userId, Long friendId);

    List<Friend> findByUserId(Long userId);
}
