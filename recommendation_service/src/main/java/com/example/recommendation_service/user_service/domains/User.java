package com.example.recommendation_service.user_service.domains;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;

import com.example.recommendation_service.AbstractAuditingEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class User extends AbstractAuditingEntity {

    private static final long serialVersionId = 1L;
    
    @Id
    private Long id;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;
    
    @Email
    @Size(min = 5, max = 254)
    @UniqueElements
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
