package com.example.user_service.domains;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.example.user_service.domains.friend_requests.FriendRequest;
import com.example.user_service.domains.friend_requests.FriendRequestStatus;

public class FriendRequestTest {
    
    @Test
    void equalsVerifier() { 
        FriendRequest request = new FriendRequest();
        request.setCreatedDate(Instant.now());
        request.setFriendId(1L);
        request.setRequestStatus(FriendRequestStatus.PENDING);
        
        assertEquals(request.getFriendId(), 1L);
        assertEquals(FriendRequestStatus.PENDING, request.getRequestStatus());

    }

}
