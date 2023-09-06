package com.example.recommendation_service.user_service.repository;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

import com.example.recommendation_service.user_service.domains.User;



public interface UserRepository extends ReactiveNeo4jRepository<User, Long> {
    
}
