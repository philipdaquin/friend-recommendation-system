package com.example.friend_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.friend_requests.FriendRequest;
import com.example.friend_service.domains.friend_requests.FriendRequestStatus;
import com.example.friend_service.repository.FriendRequestRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class FriendRequestService {
    private static final Long serialVersionId = 1L;

    private static final Logger log = LoggerFactory.getLogger(FriendRequestService.class);

    private final FriendRequestRepository repository;

    private final FriendProducerService friendProducer;

    public FriendRequestService(
        FriendRequestRepository repository, 
        FriendProducerService friendProducer
    ) { 
        this.repository = repository;
        this.friendProducer = friendProducer;
    }   
    
    /**
     * Rejects the friend request and Deletes the entity.
     * 
     * @param request 
     * @return
     */
    public Mono<Void> reject(FriendRequest request) {
        // Update entity state
        request.setRequestStatus(FriendRequestStatus.REJECTED);
        request.setAccepted(false);

        return deleteOne(request.getId())
            .doOnSuccess(i -> log.info("Rejected Friend Request"))
            .doOnError(e -> log.error("Error deleting Friend Request entity", e));
    }

    /**
     * Accepts Friend Request for the User, Adds a new Friend, deletes original Friend Request.
     * 
     * @param request
     * @return
     */
    public Mono<Friend> accept(FriendRequest request) {
        // Update the Entity's state
        request.setRequestStatus(FriendRequestStatus.ACCEPTED);
        request.setAccepted(true);

        // Update the object to the database  
        create(request);    

        // Create a new friend entity
        Friend friend = new Friend(request.getUserId(), request.getFriendId());

        // Perform dual writes to both local database and shared kafka cluster
        // On success, delete the original Friend Request Entity 
        return friendProducer.addFriendCallback(friend)
            .doOnSuccess(i -> deleteOne(request.getId()))
            .doOnError(e -> {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to execute dual writes on both external services");
            });
    }


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
     * Retrieves all Friend Requests for the User under `userId`
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
