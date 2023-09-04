package com.example.friend_service.domains;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FriendDetailTest {
    
    
    @Test
    public void equalsVerifier() {
        Friend friendOne = new Friend(1L, 2L);
        
        Friend friendTwo = new Friend(2L, 1L);

        assertEquals(friendOne.getFriendId(), friendTwo.getUserId());
    }
}
