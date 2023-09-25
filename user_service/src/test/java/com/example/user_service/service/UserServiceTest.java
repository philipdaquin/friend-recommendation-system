package com.example.user_service.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.user_service.domains.User;
import com.example.user_service.repository.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;



/**
 *  Unit Tests Service Layer 
 *  - Business Logic 
 *  - Integration with Repostory
 *  - Error Handling 
 *  - Security 
 * 
 */
// @ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    // private UserService service;

    // @Mock
    // private UserRepository repository;

    // public String FIRSTNAME = "XXXXXX";
    
    // public String LASTNAME = "AAAAAA";
    
    // public String EMAIL = "PPPPP@PPPP";

    // @BeforeEach
    // void init() { 
    //     service = new UserService(repository);
        
    // }


    // @Test
    // public void testSavedFriendReturnsSavedItemWithCallback() { 
    //     User user = new User()
    //         .firstName(FIRSTNAME)
    //         .lastName(LASTNAME)
    //         .email(EMAIL);

    //     when(repository.save(user)).thenReturn(Mono.just(user));
    //     when(repository.findById(anyLong())).thenReturn(Mono.just(user));
            
    //     Consumer<User> callback =  mock(Consumer.class);
    //     Mono<User> result = service.save(user, callback);
    //     StepVerifier.create(result)
    //         .expectNext(user)
    //         .verifyComplete();

    //     verify(callback).accept(user);
        
    // }


}
