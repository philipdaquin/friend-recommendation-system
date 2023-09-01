package com.example.user_service.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.domains.User;
import com.example.user_service.repository.UserRepository;

@Service
@Transactional
public class UserService {
    
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository repo) { 
        this.userRepository = repo;
    }


    public Optional<User> getUser(final Long id) { 
        return userRepository.findById(id);
    }

    public User save(User user) { 
        return userRepository.save(user);
    }

    // public User partialUpdate(User newUser, final Long userId) { 

    // }

    public void delete(final Long userId) {
        userRepository.deleteById(userId);
    }
}
