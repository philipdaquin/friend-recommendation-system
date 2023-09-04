package com.example.friend_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.friend_service.domains.Friend;

@SuppressWarnings("unused")
@Repository
public interface FriendRepository extends R2dbcRepository<Friend,Long> {
}
