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
import com.example.user_service.service.KafkaProducerService;
import com.example.user_service.service.RedisService;
import com.example.user_service.service.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController 
@RequestMapping("/api/v1")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final RedisService cache;
    private final UserService userService;
    private final KafkaProducerService producer;

    public UserController(
        RedisService cache,
        UserService userService,
        KafkaProducerService producer
        
    ) {
        this.cache = cache;
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
    @PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> createUser(@RequestBody @Valid Mono<User> user) throws URISyntaxException { 

        if (user.block().getId() != null) throw new UserResourceException("New User entity cannot already have an ID!");
 
        log.info("Creating a new user");
        // Flatten the Mono and map it to create a new user
        return user.flatMap(newUser -> {

            // Execute dual writes to both local persistence and shared kafka cluster  
            // If the database fails, the transaction is rollbacked else, a new event is emitted to the aggregated service
            return userService.save(newUser, cluster -> {

                try { 
                    DomainEvent<User> event = new DomainEvent<>();
                    event.setSubject(cluster);
                    event.setEventType(EventType.USER_ADDED);
                    // Send an Event to the kafka cluster 
                    producer.send(event);
                    
                    // Save User Entity to cache
                    cache.put(String.format("user-" + cluster.getId()), cluster);
    
                } catch (Exception e) {
                    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "A database transaction failed. Try again later!");
                }
            })// On success, cache the value 
            .doOnSuccess(savedValue -> {
                cache.put(String.format("user-" + savedValue.getId()), savedValue);
            });
        });
    }

    /**
     * Delete the "id" of User Entity
     * 
     * @param id
     */
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/users/{id}")
    public Mono<Void> deleteUser(@PathVariable final Long id) { 

        if (!userService.existsById(id)) { 
            throw new UserResourceException("User does not exist in the system!");
        }
        // Execute dual writes to both local persistence and shared kafka cluster  
        // If the database, the transaction is failing then a transaction is rollbacked.
        // Else, a new event is emitted to the aggregated service.
        return userService.delete(id, cluster -> {
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
        })
        // If on success, delete the cached value
        .doOnSuccess(savedValue -> {
            cache.delete(String.format("user-" + id));
        })
        .and(null);
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
    @PatchMapping(path = "/users/{id}")
    public Mono<User> partialUpdate(
        @PathVariable(value = "id", required = false) final Long userId, 
        @RequestBody @Valid final Mono<User> user
    ) throws URISyntaxException {
        // Assert that the user exists 
        if (!userService.existsById(userId)) throw new UserResourceException("User does not exist in the system!");

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
            })
            // On success, cache the value 
            .doOnSuccess(savedValue -> {
                cache.put(String.format("user-" + savedValue.getId()), savedValue);
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
    @PutMapping(path = "/users/{id}")
    public Mono<User> update(
        @PathVariable(value = "id", required = false) final Long userId, 
        @RequestBody @Valid final Mono<User> user
    ) throws URISyntaxException {
        
        // Assert that the user exists 
        if (!userService.existsById(userId)) throw new UserResourceException("User does not exist in the system!");

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
            })
            // On success, cache the value             
            .doOnSuccess(savedValue -> {
                cache.put(String.format("user-" + savedValue.getId()), savedValue);
            });
        });

    } 

    /**
     * Get the User entity based off its ID, check any Cached Value else get the value from database.
     * 
     * @param id
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> getUser(@PathVariable final Long id) {

        String key = String.format("user-" + id);

        return Mono.defer(() -> { 
            // Check the caching layer for the key-value
            return cache.get(key)
                // Else check the database 
                .switchIfEmpty(
                    userService.getOne(id)
                        // If the value is valid in our database, save to cache and return the value
                        .flatMap(user -> { 
                                return cache.put(key, user).then(Mono.just(user));
                            }
                        )
                        //  if empty, throw an `HttpStatus.INTERNAL_SERVER_ERROR`
                        .switchIfEmpty(
                            Mono.error(new HttpServerErrorException(
                                    HttpStatus.INTERNAL_SERVER_ERROR, 
                                    "Unable to find user entity"
                                )
                            )
                        )
                );
            }
        );
    }

}
