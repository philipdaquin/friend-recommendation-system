package com.example.friend_service.domains.friend_requests;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

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
    @Column(value = "accepted")
    private Boolean accepted = false;

    @NotNull
    @Column(value = "created_date")
    private Date createdDate = Date.from(Instant.now());

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
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
