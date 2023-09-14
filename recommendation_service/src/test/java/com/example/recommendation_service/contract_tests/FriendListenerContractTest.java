package com.example.recommendation_service.contract_tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.recommendation_service.friend_service.consumer.FriendConsumer;
import com.example.recommendation_service.friend_service.domains.Friend;
import com.example.recommendation_service.friend_service.domains.events.DomainEvent;
import com.example.recommendation_service.friend_service.domains.events.FriendEventType;
import com.example.recommendation_service.friend_service.service.FriendConsumerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import reactor.core.publisher.Mono;

@ExtendWith(value = {PactConsumerTestExt.class, MockitoExtension.class})
@PactTestFor(
    providerName = "friendProducerKafka", 
    providerType = ProviderType.ASYNCH,
    pactVersion = PactSpecVersion.V3
)
public class FriendListenerContractTest {

    private static final Logger log = LoggerFactory.getLogger(FriendListenerContractTest.class);
    
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String KEY_CONTENT_TYPE = "contentType";

    @InjectMocks
    private FriendConsumer consumer;

    @Mock
    private FriendConsumerService service;

    


    @Pact(consumer = "friendConsumerKafka")
    public MessagePact validFriendMessageFromKafkaProvider(MessagePactBuilder builder) { 
        PactDslJsonBody jsonBody = new PactDslJsonBody();

        jsonBody.object("subject")
                .integerType("id", 1)
                .integerType("userId", 123)
                .integerType("friendId", 23)
                .date("createdDate", "yyyy-MM-dd")
                .stringType("createdBy", "test-user")
                .stringType("lastModifiedBy", "test-user")
                .date("lastModifiedDate", "yyyy-MM-dd")
                .closeObject()
            .asBody()
            .stringValue("eventType", "FRIEND_ADDED")
            .date("createdDate", "yyyy-MM-dd")
            .stringValue("createdBy", "test-user");

        return builder
            .expectsToReceive("friendProducerKafka")
            .withMetadata(Map.of(JSON_CONTENT_TYPE, KEY_CONTENT_TYPE))
            .withContent(jsonBody)
            .toPact();
    }
    
    @Test
    @PactTestFor(pactMethod = "validFriendMessageFromKafkaProvider", providerType = ProviderType.ASYNCH)
    void shouldReceiveValidDomainEventMessage(List<Message> messages) {
        DomainEvent<Friend> event = new DomainEvent<Friend>();

        Friend friend = new Friend(1L, 123L, 23L);

        event.setSubject(friend);
        event.setEventType(FriendEventType.FRIEND_ADDED);
        event.setCreatedBy("test-user");
        event.setCreatedDate(Date.from(Instant.now()));


        log.info(event.getCreatedBy());

        when(service.apply(event)).thenReturn(Mono.just(friend));
        assertNotNull(messages);
        messages.forEach(message -> { 

            
            
            log.info(message.displayState());
            var mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructParametricType(DomainEvent.class, Friend.class);
            
            /**
             *  Errors here!
             *  - 
             * 
             * 
             */
            assertDoesNotThrow(() -> consumer.consume(
                mapper.readValue(message.contentsAsString(), type)));
                verify(service, times(1)).apply(event);
        });
        
    }
    

}
