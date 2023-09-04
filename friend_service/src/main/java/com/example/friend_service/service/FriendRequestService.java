package com.example.friend_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendRequestService {
    private static final Long serialVersionId = 1L;

    public void acceptRequest() {}
    public void rejectRequest() {}
}
