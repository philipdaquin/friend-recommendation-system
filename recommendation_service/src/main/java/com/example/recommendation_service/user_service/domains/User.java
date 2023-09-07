package com.example.recommendation_service.user_service.domains;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import com.example.recommendation_service.common.AbstractAuditingEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Node("User")
public class User extends AbstractAuditingEntity {

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
