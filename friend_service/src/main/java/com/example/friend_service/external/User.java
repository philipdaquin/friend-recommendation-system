package com.example.friend_service.external;

import java.sql.Timestamp;

public class User {

    private static final long serialVersionId = 1L;
    
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Timestamp createdDate;
    private String createdBy;
    private String lastModifiedBy;
    private Timestamp lastModifiedDate;


    public User() {}

    /**
     * Recreate the User Entity into Ubiquious Data Type 
     * 
     * @param id originated from the Id from User Service 
     * @param firstName user's firstname 
     * @param lastName user's lastname
     */
    public User(Long id, String firstName, String lastName, String email) { 
        this.userId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
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

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
