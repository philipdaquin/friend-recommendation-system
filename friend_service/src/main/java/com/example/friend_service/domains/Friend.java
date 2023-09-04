package com.example.friend_service.domains;

import java.io.Serializable;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Table(name = "friend")
public class Friend extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionId = 1L;
    
    @Id
    private Long id;

    @NotNull
    @Column(value = "user_id")
    private Long userId;

    @NotNull
    @Column(value = "friend_id")
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
