package com.example.friend_service.domains.friend_requests;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;

@Table(name = "friend_requests")
public class FriendRequest implements Serializable {

    private static final Long serialVersionId = 1L;

    @Id
    private Long id;

    @NotNull
    @Column(value = "user_id")
    private Long userId;

    @NotNull
    @Column(value = "friend_id")
    private Long friendId;

    @NotNull
    @Column(value = "created_date")
    private Instant createdDate = Instant.now();

    @Column(value = "request_status")
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
