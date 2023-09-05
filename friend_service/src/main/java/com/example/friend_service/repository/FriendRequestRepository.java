package com.example.friend_service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.friend_requests.FriendRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Repository
public interface FriendRequestRepository extends R2dbcRepository<FriendRequest, Long> {
    @Query("SELECT * FROM friend_requests WHERE user_id = $1 AND friend_id = $2")
    Mono<FriendRequest> getRequest(Long userId, Long friendId);

    @Query("SELECT * FROM friend_requests WHERE user_id = $1")
    Flux<FriendRequest> findAllByUserId(final Long userId);

    @Query("SELECT * FROM friend_requests where friend_id = $1")
    Flux<FriendRequest> findAllForUser(final Long userId);

}
