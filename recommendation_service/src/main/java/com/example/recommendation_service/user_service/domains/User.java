package com.example.recommendation_service.user_service.domains;

import java.time.Instant;
import java.util.Date;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.lang.Nullable;

import com.example.recommendation_service.common.AbstractAuditingEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Node("User")
public class User {

    private static final long serialVersionId = 1L;
    
    /*
     * Unique Identifier generated for Neo4J database
     */
    @Id
    @GeneratedValue
    private Long id;

    /** 
     * User Id 
    */
    private Long userId;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;
    
    @Email
    @Size(min = 5, max = 254)
    @UniqueElements
    private String email;

    @CreatedDate
    private Date createdDate = Date.from(Instant.now());
    
    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private Date lastModifiedDate;

    public User() {

    }

    /**
     * Recreate the User Entity to adapt to Neo4J
     * 
     * @param id originates from User Entity on User Service
     * @param firstName user's firstName 
     * @param lastName user's lastname
     */
    public User(Long id, String firstName, String lastName) { 
        this.userId = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User id(Long id) { 
        this.id = id;
        return this;
    }
    public User firstName(String firstName) { 
        this.firstName = firstName;
        return this;
    }
    public User lastName(String lastName) { 
        this.lastName = lastName;
        return this;
    }
    public User email(String email) { 
        this.email = email;
        return this;
    }
    public User userId(Long userId) { 
        this.userId = userId;
        return this;
    }

    public User createdDate(Date date) { 
        this.createdDate = date;
        return this;
    }
    public User createdBy(String createdBy) { 
        this.createdBy = createdBy;
        return this;
    }

    public User lastModifiedBy(String modifiedBy) { 
        this.lastModifiedBy = modifiedBy;
        return this;
    }
    public User lastModifiedDate(Date date) { 
        this.lastModifiedDate = date;
        return this;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
