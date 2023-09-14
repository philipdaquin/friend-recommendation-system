package com.example.friend_service.domains;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "friend")
public class Friend implements Serializable {
    private static final long serialVersionId = 1L;
    
    @Id
    private String id;

    @NotNull
    @Field(value = "user_id")
    private Long userId;

    @NotNull
    @Field(value = "friend_id")
    private Long friendId;

    @CreatedBy
    @Nullable
    @Size(max = 50)
    @Field(value = "created_date")
    public Date createdDate = Date.from(Instant.now());
    
    @CreatedDate
    @Nullable
    @Field(value = "created_by")
    public String createdBy;

    @LastModifiedBy
    @Nullable
    @Field(value = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Nullable
    @Field(value = "last_modified_date")
    private Date lastModifiedDate;


    // @PersistenceCreator
    public Friend() {}

    // @PersistenceCreator
    public Friend(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    public Friend(Long id, Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
        this.id = id.toString();
    }

        
    public Friend id(Long id) { 
        this.id = id.toString();
        return this;
    }
    public Friend userId(Long userId) { 
        this.userId = userId;
        return this;
    }
    public Friend friendId(Long friendId) { 
        this.friendId = friendId;
        return this;
    }
    public Friend createdDate(Date createdDate) { 
        this.createdDate = createdDate;
        return this;
    }
    public Friend createdBy(String createdBy) { 
        this.createdBy = createdBy;
        return this;
    }
    public Friend lastModifiedBy(String lastModifiedBy) { 
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }
    public Friend lastModifiedDate(Date lastModifiedDate) { 
        this.lastModifiedDate = lastModifiedDate;
        return this;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
