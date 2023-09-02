package com.example.user_service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.user_service.domains.User;

import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    // @Query("SELECT * FROM users x WHERE x.id = $1 LIMIT 1")
    // Mono<User> getById(Long id);
}
