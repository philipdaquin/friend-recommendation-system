package com.example.friend_service.contract_tests;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.events.DomainEvent;
import com.example.friend_service.domains.events.EventType;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;

@Disabled
// @Provider("friendProducerKafka")
// @Consumer("recommendationConsumerKafka")
// @PactBroker(url = "http://localhost:9292")
public class FriendProducerContractTest {

    // private static final String JSON_CONTENT_TYPE = "application/json";
    // private static final String KEY_CONTENT_TYPE = "contentType";

    // @BeforeEach
    // void setup(PactVerificationContext context) {
    //     context.setTarget(new MessageTestTarget());
    // }

    // @TestTemplate
    // @ExtendWith(PactVerificationInvocationContextProvider.class)
    // void pactVerfificationTestTemplate(PactVerificationContext context) { 
    //     context.verifyInteraction();
    // }

    // @PactVerifyProvider("friendProducerKafka")
    // MessageAndMetadata verifyMessage() { 
    //     // Friend friend = new Friend(1L, 123L, 23L);
    //     Friend friend = new Friend()    
    //         .id(1L)
    //         .userId(123L)
    //         .friendId(23L)
    //         .createdBy("test-user")
    //         .createdDate(Date.from(Instant.now()))
    //         .lastModifiedDate(Date.from(Instant.now()))
    //         .lastModifiedBy("test-friend");

    //     DomainEvent<Friend> event = new DomainEvent<Friend>();
    //     event.setCreatedBy("test-friend");
    //     event.setCreatedDate(Date.from(Instant.now()));
    //     event.setSubject(friend);
    //     event.setEventType(EventType.FRIEND_ADDED);

    //     JsonSerializer<DomainEvent<Friend>> serializer = new JsonSerializer<DomainEvent<Friend>>();
        
    //     return new MessageAndMetadata(
    //         serializer.serialize("friend", event), 
    //         Map.of(KEY_CONTENT_TYPE, JSON_CONTENT_TYPE)
    //     );
    // }
}
