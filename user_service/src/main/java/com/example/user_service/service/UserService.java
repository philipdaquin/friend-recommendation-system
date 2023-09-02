package com.example.user_service.service;

import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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



    public Mono<User> getOne(final Long id) { 
        return userRepository.findById(id);
    }


    /**
     * 
     * Insert the User with a supplied Callback that sends out an event to a shared messaging queue 
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

    public Mono<User> partialUpdate(User newUser, final Long userId) { 
        return userRepository.findById(userId)
            .flatMap(current -> { 

                if (newUser.getFirstName() != null) current.setFirstName(newUser.getFirstName());
                if (newUser.getLastName() != null) current.setLastName(newUser.getLastName());
                if (newUser.getEmail() != null) current.setEmail(newUser.getEmail());


                return userRepository.save(current);
            }).switchIfEmpty(userRepository.save(newUser));
    }

    public void delete(final Long userId) {
        userRepository.deleteById(userId);
    }
}
