package com.example.recommendation_service.friend_service.domains;
import com.example.recommendation_service.common.AbstractAuditingEntity;


public class Friend extends AbstractAuditingEntity  {
    private static final long serialVersionId = 1L;
    
    private Long id;
    private Long userId;
    private Long friendId;

    public Friend() {}

    public Friend(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    
}
