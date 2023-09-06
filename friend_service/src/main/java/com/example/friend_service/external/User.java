package com.example.friend_service.external;

import com.example.friend_service.domains.AbstractAuditingEntity;

public class User extends AbstractAuditingEntity {

    private static final long serialVersionId = 1L;
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;


    public User() {}

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
