package com.example.friend_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.events.DomainEvent;
import com.example.friend_service.domains.events.EventType;

import reactor.core.publisher.Mono;

@Service
@Transactional
public class FriendProducerService {
    private static final Logger log = LoggerFactory.getLogger(FriendService.class);

    private final FriendService friendService;

    private final KafkaProducerService producer;

    public FriendProducerService(
        FriendService friendService, 
        KafkaProducerService producer
    ) { 
        this.friendService = friendService;
        this.producer = producer;
    }

    /**
     * Adds a new friend with callback.
     * 
     * Performs dual write to both external services with local database and a shared kafka cluster.
     * If an error takes place at any stage, the transaction is immediately rolledback
     * 
     * @param friend
     * @return
     */
    public Mono<Friend> addFriendCallback(Friend friend) { 
        return friendService.save(friend, entity -> { 
            try { 
                // Create a Domain Event to emit 
                DomainEvent<Friend> event = new DomainEvent<>();
                // Set the entity payload after it has been updated by database, but before committing 
                event.setSubject(entity);
                event.setEventType(EventType.FRIEND_ADDED);

                // Emit a DomainEvent to perform dual writes to Kafka cluster
                producer.send(event);

            } catch (Exception e ) { 
                log.error(String.format("A dual-write to local database and shared cluster failed!" + entity.toString()), e);
                
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "A database transaction failed. Try again later!");
            }
        });
    }

    /**
     * Removes friend with callback.
     * 
     * Performs dual write to both external services with local database and a shared kafka cluster.
     * If an error takes place at any stage, the transaction is immediately rolledback
     * @param friend
     * @return
     */
    public Mono<Friend> removeFriendCallback(Friend friend) { 
        return friendService.deleteOne(friend, entity -> { 
            try { 
                // Create a Domain Event to emit 
                DomainEvent<Friend> event = new DomainEvent<>();
                // Set the entity payload after it has been updated by database, but before committing 
                event.setSubject(entity);
                event.setEventType(EventType.FRIEND_REMOVED);
                // Emit a DomainEvent to perform dual writes to Kafka cluster
                producer.send(event);
            
            } catch (Exception e ) { 
                log.error(String.format("A dual-write to local database and shared cluster failed!" + entity.toString()), e);
                
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "A database transaction failed. Try again later!");
            }
        });        
    }
}
