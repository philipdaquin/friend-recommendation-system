package com.example.user_service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
// import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
// import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

import com.example.user_service.domains.User;

import reactor.core.publisher.Mono;

/**
 * Spring Data SQL repository for the User entity.
 */
// @EnableR2dbcRepositories
// @SuppressWarnings("unused")
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT * FROM users x WHERE x.id = $1 LIMIT 1")
    Mono<User> getById(Long id);
}
