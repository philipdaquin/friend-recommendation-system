package com.example.recommendation_service.repository;

import java.time.Instant;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.example.recommendation_service.user_service.domains.User;


@SuppressWarnings("unused")
@Repository
public interface UserRepository extends ReactiveNeo4jRepository<User, Long> {

    // Match the user by the given UserID
    @Query("MATCH (user:USER {userId: {0}})")
    User findUserByUserId(Long userId);


    // Create a relationship between userA and userB
    @Query("""
        MATCH (userA: User), (userB: User)
        WHERE userA.userId={0} AND userB.userId={1}    
        CREATE (userA)-[:FRIEND { createdDate: {2}, lastModifiedDate: {3}}]->(userB)
    """)
    void addFriend(Long userId, Long friendId, Instant createdDate, Instant lastModifiedDate);
    
    @Query("""
        MATCH (userA: User)-[relation:FRIEND]->(userB: User)
        WHERE userA.userId={0} AND userB.userId={1}
        DELETE relation
    """)
    void removeFriend(Long userId, Long friendId);

    // Find the FRIEND that both userA and userB shares
    @Query("""
        MATCH (userA: USER), (userB: User)
        WHERE userA.userId={0} AND userB.userId={1}                
        MATCH (userA)-[:FRIEND]-(mutuals: User)-[:FRIEND]-(userB)
        RETURN DISTINCT mutuals

    """)
    Streamable<User> findMutualFriends(Long userId, Long friendId);

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
        WHERE userA.userId={0}

        WHERE NOT (userA)-[:FRIEND]-(nonFriends)
        WITH nonFriends, count(nonFriends) as mutualFriends

        RETURN nonFriends as user, mutualFriends as weight
        ORDER BY mutualFriends DESC
    """)    
    <T> Streamable<T> recommendFriends(Long userId, Class<T> clazz);
}
