package com.example.recommendation_service.repository;

import java.time.Instant;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.example.recommendation_service.user_service.domains.User;


@SuppressWarnings("unused")
@Repository
public interface UserRepository extends ReactiveNeo4jRepository<User, Long> {
    
    @Query("MATCH (user:USER {userId: {0}})")
    User findUserByUserId(Long userId);

    @Query(
        """ 
            
        """
    )
    void addFriend(Long userId, Long friendId, Instant createdAt, Instant lastModifiedTime);
    
    @Query("")
    void removeFriend();

    @Query("")
    void findMutualFriends();
    

    @Query("")    
    void recommendFriends();
}
