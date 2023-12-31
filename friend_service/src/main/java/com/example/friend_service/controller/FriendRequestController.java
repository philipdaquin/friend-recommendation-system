package com.example.friend_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.example.friend_service.domains.friend_requests.FriendRequest;
import com.example.friend_service.domains.friend_requests.FriendRequestStatus;
import com.example.friend_service.repository.FriendRequestRepository;
import com.example.friend_service.service.FriendRequestService;

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/v1")
public class FriendRequestController {
    
    private static final Logger log = LoggerFactory.getLogger(FriendRequestController.class);

    private final FriendRequestRepository repository;

    private final FriendRequestService service;

    public FriendRequestController(
        FriendRequestRepository repository,
        FriendRequestService service
    ) { 
        this.repository = repository;
        this.service = service;
    }


    /**
     * Accepts Friend Request and adds Friend to the User 
     * 
     * @param id
     * @param requestId
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(path = "/users/{id}/request/{requestId}/command/accept-request")
    public Mono<Void> acceptRequest(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id, 
        @NotNull(message = "RequestId cannot be null") @PathVariable final Long requestId 
    ) {

        // Find the FriendRequest entity under the given UserID and a FriendRequest Entity with RequestID 
        return service.getOneWithIdAndUser(id, requestId)
            .doOnNext(entity -> { 
                // Check if Friend Request entity exist 
                if (entity == null) 
                    throw new HttpClientErrorException(HttpStatus.CONFLICT, "Invalid Request entity; Entity not found!");
        
                // Check if the Friend Request entity is valid
                if (entity.getRequestStatus() != FriendRequestStatus.PENDING)
                    throw new HttpClientErrorException(HttpStatus.CONFLICT, 
                        "To accept new Friend Request, the status must be PENDING!" 
                        + "Entity Status:" 
                        + entity.getRequestStatus().toString());
            })
            .doOnSuccess(request -> service.accept(request))
            .doOnError(e -> {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "A database transaction failed. Try again later!");
            }).then();

    }

    /**
     * Reject Friend Request and Deletes Entity 
     * 
     * @param id
     * @param requestId
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping(path = "/users/{id}/request/{requestId}/command/reject-request")
    public Mono<Void> rejectRequest(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id, 
        @NotNull(message = "RequestId cannot be null") @PathVariable final Long requestId 
    ) {
        // Find the FriendRequest entity under the given UserID and a FriendRequest Entity with RequestID 
        return service.getOneWithIdAndUser(id, requestId)
            .doOnNext(entity -> { 
                // Check if Friend Request entity exist 
                if (entity == null) 
                    throw new HttpClientErrorException(HttpStatus.CONFLICT, "Invalid Request entity; Entity not found!");
        
                // Check if the Friend Request entity is valid
                if (entity.getRequestStatus() != FriendRequestStatus.PENDING)
                    throw new HttpClientErrorException(HttpStatus.CONFLICT, 
                        "To reject new Friend Request, the status must be PENDING!" 
                        + "Entity Status:" 
                        + entity.getRequestStatus().toString());
            })
            .doOnSuccess(request -> service.reject(request))
            .doOnError(e -> {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "A database transaction failed. Try again later!");
            }).then();

    }


    /**
     * Create a new friend request 
     * 
     * @param id
     * @param friendId
     * @return
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(path = "/users/{id}/request", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<FriendRequest> createRequest(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id, 
        @NotNull(message = "FriendId cannot be empty") @RequestParam(required = true, value = "friendId") final Long friendId 
    ) {
        
        // Check if the request already exist 
        if (service.getRequest(id, friendId).single() != null)
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Unabled to befriend yourself");

        FriendRequest request = new FriendRequest(id, friendId);

        return service.create(request);
    }

    /**
     * Delete Request by `requestId`
     * 
     * @param requestId
     * @return
     */
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/users/{id}/request/{requestId}")
    public Mono<Void> deleteRequest(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id, 
        @NotNull(message = "RequestId cannot be null") @PathVariable final Long requestId 
    ) {

        // Check if Friend Request already exists
        if (!repository.existsById(requestId.toString()).block()) 
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Unabled to befriend yourself");

        // Check if the request was created by the user 
        return service.getOneWithIdAndUser(id, requestId)   
            .flatMap(entity -> { 
                return service.deleteOne(entity.getId());
            }).and(null);
    }

    /**
     * Shows all the requests sent by the user 
     * 
     * @param id
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/users/{id}/friend/from/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FriendRequest> getFriendRequestsByUser(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id 
    ) {
        return service.getAllByUser(id).collectList().block();
    }   

    /**
     * Retrieves all Friend Request entities for the user 
     * 
     * @param id
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/users/{id}/friend/to/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FriendRequest> getFriendRequestsForUser(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id 
    ) {
        return service.getAllForUser(id).collectList().block();
    }

}
