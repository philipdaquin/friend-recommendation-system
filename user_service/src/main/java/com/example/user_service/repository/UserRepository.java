package com.example.user_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.stereotype.Repository;

import com.example.user_service.domains.User;

/**
 * Spring Data SQL repository for the User entity.
 */
@SuppressWarnings("unused")
@Repository
@EnableR2dbcRepositories
public interface UserRepository extends R2dbcRepository<User, Long> {

    // @Query("SELECT * FROM users x WHERE x.id = $1 LIMIT 1")
    // Mono<User> getById(Long id);
}
