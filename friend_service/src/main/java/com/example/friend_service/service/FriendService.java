package com.example.friend_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.repository.FriendRepository;

@Service
@Transactional
public class FriendService {
    
    private static final Long serialVersionId = 1L;

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) { 
        this.friendRepository = friendRepository;
    }

    public List<Friend> getAllByUserId(final Long userId) {
        return friendRepository.findAllByUserId(userId)
    }
    public void getOne() {}
    public void addFriend() {}
    public void removeFriend() {}
    

}
