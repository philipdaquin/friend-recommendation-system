package com.example.recommendation_service.friend_service.domains.friend_requests;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotNull;

public class FriendRequest implements Serializable {

    private static final Long serialVersionId = 1L;

    @Id
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long friendId;

    @NotNull
    private Boolean accepted = false;

    @NotNull
    private Instant createdDate = Instant.now();

    private FriendRequestStatus requestStatus = FriendRequestStatus.PENDING;

    public Long getId() {
        return id;
    }

    public FriendRequest(Long userId, Long friendId) { 
        this.userId = userId;
        this.friendId = friendId;
    }


    public FriendRequest(Long userId, Long friendId, FriendRequestStatus requestStatus) { 
        this.userId = userId;
        this.friendId = friendId;
        this.requestStatus = requestStatus;
    }
        

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public FriendRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(FriendRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
