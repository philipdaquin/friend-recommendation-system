package com.example.user_service.domains;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionId = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(value = "user_id")
    // private Long userId;

    @Size(max = 50)
    @Column(value = "first_name")
    private String firstName;

    @Size(max = 50)
    @Column(value = "last_name")
    private String lastName;
    
    @Email
    @Size(min = 5, max = 254)
    @UniqueElements
    @Column(value = "email")
    private String email;

    @CreatedBy
    @Nullable
    @Size(max = 50)
    @Column(value = "created_date")
    private Date createdDate = Date.from(Instant.now());
    
    @CreatedDate
    @Nullable
    @Column(value = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Nullable
    @Column(value = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Nullable
    @Column(value = "last_modified_date")
    private Date lastModifiedDate;


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

    // public Long getUserId() {
    //     return userId;
    // }

    // public void setUserId(Long userId) {
    //     this.userId = userId;
    // }

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

    public User() {}


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

    // public User userId(Long userId) { 
    //     this.userId = userId;
    //     return this;
    // }
    
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
}
