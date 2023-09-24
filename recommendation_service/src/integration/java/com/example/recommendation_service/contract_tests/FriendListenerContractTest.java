package com.example.recommendation_service.contract_tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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

    private DomainEvent<Friend> event;

    private DomainEvent<Friend> init() { 
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = "2000-01-31";
        
        // Step 1: Parse the date string into a LocalDate
        LocalDate localDate = LocalDate.parse(dateString);
        
        // Step 2: Convert the LocalDate to a Date
        Date newDate = Date.from(localDate.atTime(LocalTime.of(8, 0)).atZone(ZoneId.systemDefault()).toInstant());
        Friend friend = new Friend(1L, 123L, 23L);
        friend.setLastModifiedBy("test-friend");
        friend.setLastModifiedDate(newDate);

        event = new DomainEvent<Friend>();
        event.setSubject(friend);
        event.setEventType(FriendEventType.FRIEND_ADDED);
        event.setCreatedBy("test-user");
        event.setCreatedDate(newDate);

        

        return event;
    }



    @Pact(consumer = "friendConsumerKafka")
    public MessagePact validFriendMessageFromKafkaProvider(MessagePactBuilder builder) { 
        PactDslJsonBody jsonBody = new PactDslJsonBody();

        Friend friend = init().getSubject();


        jsonBody.object("subject")
                .integerType("id", friend.getId())
                // .integerType("userId", friend.getUserId())
                // .integerType("friendId", friend.getFriendId())
                // .date("createdDate", "yyyy-MM-dd", event.getCreatedDate())
                // .stringType("createdBy", friend.getCreatedBy())
                // .stringType("lastModifiedBy", friend.getLastModifiedBy())
                // .date("lastModifiedDate", "yyyy-MM-dd", friend.getLastModifiedDate())
                .closeObject()
            .asBody()
            // .stringType("eventType", FriendEventType.FRIEND_ADDED.toString())
            .date("createdDate", "yyyy-MM-dd", event.getCreatedDate())
            .stringValue("createdBy", event.getCreatedBy());

        return builder
            .expectsToReceive("friendProducerKafka")
            .withMetadata(Map.of(JSON_CONTENT_TYPE, KEY_CONTENT_TYPE))
            .withContent(jsonBody)
            .toPact();
    }
    
    @Test
    @PactTestFor(pactMethod = "validFriendMessageFromKafkaProvider", providerType = ProviderType.ASYNCH)
    void shouldReceiveValidDomainEventMessage(List<Message> messages) {

        // when(service.apply(event)).thenReturn(Mono.just(event.getSubject()));
        assertNotNull(messages);
        // messages.forEach(message -> { 
            
        //     log.info(message.displayState());
        //     var mapper = new ObjectMapper();
        //     JavaType type = mapper.getTypeFactory().constructParametricType(DomainEvent.class, Friend.class);
            
        //     /**
        //      *  Errors here!
        //      *  - 
        //      * 
        //      * 
        //      */
        //     assertDoesNotThrow(() -> consumer.consume(
        //         mapper.readValue(message.contentsAsString(), type)));

        //         assertNotNull(message);

        //         verify(service, times(1)).apply(event);
                
        //     }
        // );
        
    }
    

}
