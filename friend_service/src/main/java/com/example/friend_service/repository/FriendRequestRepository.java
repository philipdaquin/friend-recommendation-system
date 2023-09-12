package com.example.friend_service.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.friend_requests.FriendRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Repository
public interface FriendRequestRepository extends ReactiveMongoRepository<FriendRequest, String> {
    
    // @Query("SELECT * FROM friend_requests WHERE user_id = $1 AND friend_id = $2")
    /**
     * 
     * @param userId
     * @param friendId
     * @return
     */
    @Query("{'user_id' : ?0 , 'friend_id' : ?1}")
    Mono<FriendRequest> getRequest(Long userId, Long friendId);


    /**
     * 
     * 
     * @param userId
     * @return
     */
    @Query("{'user_id' : ?0}")
    Flux<FriendRequest> findAllByUserId(final Long userId);

    /**
     * 
     * 
     * @param userId
     * @return
     */
    @Query("{'friend_id' : ?0}")
    Flux<FriendRequest> findAllForUser(final Long userId);


    /**
     * 
     * 
     * @param userId
     * @param requestId
     * @return
     */
    @Query("{'user_id' : ?0 , 'friend_id' : ?1}")
    Mono<FriendRequest> getRequestWithUser(Long userId, Long requestId);
}
