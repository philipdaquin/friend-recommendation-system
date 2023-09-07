package com.example.user_service.controller;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.example.user_service.domains.User;
import com.example.user_service.domains.events.DomainEvent;
import com.example.user_service.domains.events.EventType;
import com.example.user_service.errors.UserResourceException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.KafkaProducerService;
import com.example.user_service.service.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController 
@RequestMapping("/api/v1")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserService userService;
    private final KafkaProducerService producer;

    public UserController(
        UserRepository userRepository,
        UserService userService,
        KafkaProducerService producer
        
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.producer = producer;
    }

    /**
     * Create a new User entity
     * 
     * @param user
     * @return
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(path = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> createUser(@RequestBody @Valid Mono<User> user) throws URISyntaxException { 

        if (user.block().getId() != null) throw new UserResourceException("New User entity cannot already have an ID!");
 
        log.info("Creating a new user");
        // Flatten the Mono and map it to create a new user
        return user.flatMap(newUser -> {

            // Execute dual writes to both local persistence and shared kafka cluster  
            // If the database, the transaction is rollback else, a new event is emitted to the aggregated service
            return userService.save(newUser, cluster -> {

                try { 
                    DomainEvent<User> event = new DomainEvent<>();
                    event.setSubject(cluster);
                    event.setEventType(EventType.USER_ADDED);
                    // Send an Event to the kafka cluster 
                    producer.send(event);
    
                } catch (Exception e) {
                    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "A database transaction failed. Try again later!");
                }
            });
        });
    }

    /**
     * Delete the "id" of User Entity
     * 
     * @param id
     */
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/user/{id}")
    public void deleteUser(@PathVariable final Long id) { 

        if (!userRepository.existsById(id).block()) { 
            throw new UserResourceException("User does not exist in the system!");
        }
        // Execute dual writes to both local persistence and shared kafka cluster  
        // If the database, the transaction is failing then a transaction is rollbacked.
        // Else, a new event is emitted to the aggregated service.
        userService.delete(id, cluster -> {
            try { 
                DomainEvent<User> event = new DomainEvent<>();
                event.setSubject(cluster);
                event.setEventType(EventType.USER_REMOVED);
                // Send an Event to the kafka cluster 
                producer.send(event);

            } catch (Exception e) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "A database transaction failed. Try again later!");
            }
        });
    }

    /**
     * Partial updates given fields of an existing User entity fields
     * 
     * @param userId
     * @param user
     * @return
     * @throws URISyntaxException
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @PatchMapping(path = "/user/{id}")
    public Mono<User> partialUpdate(
        @PathVariable(value = "id", required = false) final Long userId, 
        @RequestBody @Valid final Mono<User> user
    ) throws URISyntaxException {
        // Assert that the user exists 
        if (!userRepository.existsById(userId).block()) throw new UserResourceException("User does not exist in the system!");

        // Assert that the user id and id are the same
        if (userId != user.block().getId()) throw new UserResourceException("Invalid Id");

        // Assert that id in user is not empty
        if (user.block().getId() == null) throw new UserResourceException("ID not found");

        // Flatten the Mono and map it to create a new user
        return user.flatMap(newUser -> {

            // Execute dual writes to both local persistence and shared kafka cluster  
            // If the database, the transaction is rollback else, a new event is emitted to the aggregated service
            return userService.partialUpdate(newUser, cluster -> {

                try { 
                    DomainEvent<User> event = new DomainEvent<>();
                    event.setSubject(cluster);
                    event.setEventType(EventType.USER_UPDATED);
                    // Send an Event to the kafka cluster 
                    producer.send(event);
    
                } catch (Exception e) {
                    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "A database transaction failed. Try again later!");
                }
            });
        });
    }

    /**
     * Updates an existing User details 
     * 
     * @param userId
     * @param user
     * @return
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @PutMapping(path = "/user/{id}")
    public Mono<User> update(
        @PathVariable(value = "id", required = false) final Long userId, 
        @RequestBody @Valid final Mono<User> user
    ) throws URISyntaxException {
        
        // Assert that the user exists 
        if (!userRepository.existsById(userId).block()) throw new UserResourceException("User does not exist in the system!");

        // Assert that the user id and id are the same
        if (userId != user.block().getId()) throw new UserResourceException("Invalid Id");

        // Assert that id in user is not empty
        if (user.block().getId() == null) throw new UserResourceException("ID not found");

        // Flatten the Mono and map it to create a new user
        return user.flatMap(newUser -> {

            // Execute dual writes to both local persistence and shared kafka cluster  
            // If the database, the transaction is rollback else, a new event is emitted to the aggregated service
            return userService.save(newUser, cluster -> {

                try { 
                    DomainEvent<User> event = new DomainEvent<>();
                    event.setSubject(cluster);
                    event.setEventType(EventType.USER_UPDATED);
                    // Send an Event to the kafka cluster 
                    producer.send(event);
    
                } catch (Exception e) {
                    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "A database transaction failed. Try again later!");
                }
            });
        });

    } 

    /**
     * Get the "ID" User Entity
     * 
     * @param id
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> getUser(@PathVariable final Long id) {
        return userService.getOne(id);
    }

}
