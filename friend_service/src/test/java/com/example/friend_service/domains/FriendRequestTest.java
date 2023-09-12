package com.example.friend_service.domains;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.friend_service.domains.friend_requests.FriendRequest;

public class FriendRequestTest {
    
    @Test
    public void equalsVerifier() { 
        
        Long userId = 1L, friendId = 10L;
        FriendRequest request = new FriendRequest(userId, friendId);

        assertNotNull(request);
        assertEquals(userId, request.getUserId());
        assertEquals(friendId, request.getFriendId());
        
    }
}
