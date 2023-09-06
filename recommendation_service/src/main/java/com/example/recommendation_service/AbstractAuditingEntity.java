package com.example.recommendation_service;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;

public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionId = 1L;

    @CreatedBy
    @Nullable
    @Size(max = 50)
    @JsonIgnore
    public Instant createdDate = Instant.now();
    
    @CreatedDate
    @JsonIgnore    
    public String createdBy;

    @LastModifiedBy
    @JsonIgnore    
    private String lastModifiedBy;

    @LastModifiedDate
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
