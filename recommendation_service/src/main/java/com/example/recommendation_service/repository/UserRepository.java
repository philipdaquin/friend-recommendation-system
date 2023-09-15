package com.example.recommendation_service.repository;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.example.recommendation_service.friend_service.domains.Friend;
import com.example.recommendation_service.user_service.domains.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


// @SuppressWarnings("unused")
// @Repository
public interface UserRepository extends ReactiveNeo4jRepository<User, Long> {





    /**
     * 
     * 
     * 
     * @param userId
     * @param friendId
     * @return
     */
    @Query("""
        MATCH (userA:User {userId: $userId})
        OPTIONAL MATCH (userA)-[:FRIEND]->(userB:User {userId: $friendId})
        RETURN COALESCE(EXISTS((userA)-[:FRIEND]->(userB)), false) AS exists;
        """)
    Mono<Boolean> findRelationExist(Long userId, Long friendId);



    @Query("""
            MATCH (userA:User {userId: $userId})
            RETURN COUNT { (userA)-[:FRIEND]->(:User) } as count
            """)
    Mono<Integer> findAllByUserId(Long userId);

    /**
     * 
     * // Match the user by the given UserID
     * 
     * 
     * @param userId
     * @return
     */
    @Query("""
            MATCH (user: User)
            WHERE user.userId = $userId
            RETURN user
        """)
    Mono<User> findUserByUserId(Long userId);


    /**
     * 
     * /**
     *  Create a relationship between userA and userB

    
        CREATE (userA)-[r: FRIEND { 
            createdDate : $createdDate, 
            lastModifiedDate : $lastModifiedDate 
        }]->(userB)
     
     * 
     * 
     * @param userId
     * @param friendId
     * @param createdDate
     * @param lastModifiedDate
     * @return
     */  
    @Query("""
        MATCH (userA: User {userId: $userId})
        CREATE (userA)-[:FRIEND]->(userB: User {userId: $friendId}) 
        RETURN EXISTS ((userA)-[:FRIEND]->(userB)) AS exists
        LIMIT 1

    """)
    Mono<Boolean> addFriend(Long userId, Long friendId, Date createdDate, Date lastModifiedDate);
    
    /**
     * 
     * @param userId
     * @param friendId
     * @return
     */
    @Query("""
        MATCH (userA:User {userId: $userId})-[relation:FRIEND]->(userB:User {userId: $friendId})
        DELETE relation
        RETURN EXISTS ((userA)-[:FRIEND]->(userB)) AS exists
        LIMIT 1
    """)
    Mono<Boolean> removeFriend(Long userId, Long friendId);

    // Find the FRIEND that both userA and userB shares
    /**
     * DISTINCT
     * 
     * 
     * @param userId
     * @param friendId
     * @return
     */
    @Query("""
        MATCH (userA:User {userId: $userId})-[:FRIEND]-(mutuals:User)-[:FRIEND]-(userB:User {userId: $friendId})
        RETURN DISTINCT mutuals
    """)
    Flux<User> findMutualFriends(Long userId, Long friendId);

    /**
     * Find all the Friends of your Friends
     * 
     * Remove the Friends that your Friends with 
     * 
     * Count the Number of Mutuals 
     * 
     * Rank Friends based on the number of Mutuals
     * 
     * @param <T>
     * @param userId
     * @param clazz
     * @return All Non Mutual Friends
     */
    @Query("""
        MATCH (userA: User)-[:FRIEND]-(friends), (nonFriends: User)-[:FRIEND]-(friends)
        WHERE userA.userId = $userId 
        AND NOT (userA)-[:FRIEND]-(nonFriends)
        WITH nonFriends, count(nonFriends) as mutualFriends

        RETURN nonFriends as user, mutualFriends as weight
        ORDER BY mutualFriends DESC
    """)    
    <T> Flux<T> recommendFriends(Long userId, Class<T> clazz);
}
