package com.example.friend_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.example.friend_service.domains.friend_requests.FriendRequest;
import com.example.friend_service.repository.FriendRequestRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class FriendRequestService {
    private static final Long serialVersionId = 1L;

    private final FriendRequestRepository repository;

    public FriendRequestService(FriendRequestRepository repository) { 
        this.repository = repository;
    }   

    public void acceptRequest() {}
    public void rejectRequest() {}

    public void getRequestsForUser() {}

    /**
     * Create a new Friend Request entity 
     * 
     * @param request
     * @return
     */
    public Mono<FriendRequest> create(FriendRequest request) {

        if (request.getUserId() == request.getFriendId())
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Unabled to befriend yourself");
        
        // Ensure both UserId and FriendId fields arent null
        if (request.getUserId() == null ||  request.getFriendId() == null) 
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Invalid friend request object");

        return repository.save(request);
    }

    /**
     * Retries all Friend Request Entities under `userId`
     * 
     * @param userId
     * @return
     */
    public Flux<FriendRequest> getAllByUser(final Long userId) {
        return repository.findAllByUserId(userId);
    }
    
    /**
     * Deletes a Friend Request entity by Id
     * 
     * @param id
     * @return
     */
    public Mono<Void> deleteOne(final Long id) {
        return repository.deleteById(id);
    }

    /**
     * Get Friend Request that involves userId and friendId
     * 
     * @param userId
     * @param friendId
     * @return
     */
    public Mono<FriendRequest> getRequest(final Long userId, final Long friendId) { 
        return repository.getRequest(userId, friendId);
    }

    /**
     * 
     * 
     * @param userId
     * @return
     */
    public Flux<FriendRequest> getAllForUser(final Long userId) { 
        return repository.findAllForUser(userId);
    }


    /**
     * Retrieves the Friend Request Entity using UserId and Request Id
     * 
     * @param userId
     * @param requestId
     * @return
     */
    public Mono<FriendRequest> getOneWithIdAndUser(final Long userId, final Long requestId) { 
        return repository.getRequestWithUser(userId, requestId);
    }
}
