package com.example.recommendation_service.friend_service.domains;
import java.util.Date;



public class Friend   {
    private static final long serialVersionId = 1L;
    
    private Long id;
    private Long userId;
    private Long friendId;
    private Date createdDate;
    private String createdBy;
    private String lastModifiedBy;
    private Date lastModifiedDate;

    public Friend() {}

    public Friend(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    public Friend(Long id, Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
        this.id = id;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    
}
