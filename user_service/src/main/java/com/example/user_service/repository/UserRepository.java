package com.example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user_service.domains.User;

@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
