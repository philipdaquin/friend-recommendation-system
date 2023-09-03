package com.example.user_service.domains;

import java.io.Serializable;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Table(name = "users")
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionId = 1L;
    
    @Id
    private Long id;

    @Size(max = 50)
    @Column(value = "first_name")
    private String firstName;

    @Size(max = 50)
    @Column(value = "first_name")
    private String lastName;
    
    @Email
    @Size(min = 5, max = 254)
    @UniqueElements
    @Column(value = "email")
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
