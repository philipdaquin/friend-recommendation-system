package com.example.user_service.service;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.example.user_service.domains.User;
import com.example.user_service.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
@Transactional
public class UserService {
    
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository repo) { 
        this.userRepository = repo;
    }


    /**
     * Get one User entity by id 
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Mono<User> getOne(final Long id) { 
        return userRepository
            .findById(id)
            .single();
    }

    /**
     * 
     * Insert the User with a supplied Callback that sends out an event to a shared messaging 
     * queue like Apache kafka before finalising the transaction.
     * 
     * @param user an entity to create 
     * @param callback is a {@link Consumer<User>} that will allow you to throw an exception to rollback the transaction.
     * @return a {@link Mono<User>} that emits the result of the transaction in the form of the committed {@link User}
     */
    public Mono<User> save(User user, Consumer<User> callback) { 
        return userRepository
            .save(user)
            .map(User::getId)
            .flatMap(userId -> userRepository
                .findById(userId)
                .single()
                .delayUntil(item -> Mono.fromRunnable(() -> callback.accept(item))));
    }


    /**
     * Update basic information for the user with a supplied Callback from a shared messaging queue 
     * like Apache Kafka before finalisng the transaction
     * 
     * @param newUser
     * @param userId
     * @return
     */
    public Mono<User> partialUpdate(User newUser, Consumer<User> callback) { 
        Assert.notNull(newUser.getId(), "");
        
        AtomicReference<Long> userId = new AtomicReference<Long>();
        userId.set(newUser.getId());

        return userRepository.findById(userId.get())
            .flatMap(
                current -> { 

                    if (newUser.getFirstName() != null) current.setFirstName(newUser.getFirstName());
                    if (newUser.getLastName() != null) current.setLastName(newUser.getLastName());
                    if (newUser.getEmail() != null) current.setEmail(newUser.getEmail());
                    return userRepository.save(current);
                }
            )
            
            .delayUntil(item -> Mono.fromRunnable(() -> callback.accept(item)))
            .single();
            
    }

    /**
     * 
     * Deletes an entity; attempts to perform dual writes on both local persistence and 
     * a shared messaging cluster, like Apache kafka
     * 
     * @param userId
     * @param callback
     */
    public void delete(final Long userId, Consumer<User> callback) {
       
        userRepository
            .findById(userId)
            .flatMap(user -> { 
                return userRepository.delete(user).then(Mono.just(user));
            })
            .delayUntil(item -> Mono.fromRunnable(() -> callback.accept(item)));
    }
}
