package com.example.friend_service.external;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 *  A Web client to fetch User Details 
 * 
 */
@Service
public class UserClient {
    
    private final WebClient.Builder client;

    public UserClient(WebClient.Builder client) { 
        this.client = client;
    }

    String baseUrl = "http://user-service/";
    String baseUrlEntity = " api/v1/user/{id}";

    /**
     * Fetch the User details from http://user-service/ api/v1/user/{userId}
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    public Mono<User> getUser(final Long userId) { 
        Mono<User> user;
        
        try { 
            user = client.baseUrl(baseUrl)
                .build()
                .get()
                .uri(baseUrl + baseUrlEntity, userId)
                .retrieve()
                .bodyToMono(User.class)
                .single()
                .log();
            
            return user;

        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Unable to retrieve the user");
        } 
    }

}
