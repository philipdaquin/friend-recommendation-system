package com.example.user_service.domains;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;

public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionId = 1L;

    @CreatedBy
    @Nullable
    @Size(max = 50)
    @Column(value = "created_by")
    @JsonIgnore
    public Instant createdDate = Instant.now();
    
    @CreatedDate
    @Column(value = "created_date")
    @JsonIgnore    
    public String createdBy;

    @LastModifiedBy
    @Column(value = "last_modified_by")
    @JsonIgnore    
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(value = "last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
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

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    

}
