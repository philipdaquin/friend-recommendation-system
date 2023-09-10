package com.example.user_service.domains.friend_requests;

import java.io.Serializable;
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
    @Column(value = "friend_id")
    private Long friendId;

    @NotNull
    @Column(value = "created_date")
    private Date createdDate = Date.from(Instant.now());

    @Column(value = "request_status")
    private FriendRequestStatus requestStatus = FriendRequestStatus.PENDING;

    public Long getId() {
        return id;
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

    public FriendRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(FriendRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
