package com.example.recommendation_service.contract_tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.example.recommendation_service.friend_service.domains.Friend;
import com.example.recommendation_service.user_service.consumer.UserConsumer;
import com.example.recommendation_service.user_service.domains.User;
import com.example.recommendation_service.user_service.domains.events.DomainEvent;
import com.example.recommendation_service.user_service.domains.events.UserEventType;
import com.example.recommendation_service.user_service.service.UserConsumerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.MessagePact;
import reactor.core.publisher.Mono;
import au.com.dius.pact.core.model.messaging.Message;

@ExtendWith(value = {PactConsumerTestExt.class, MockitoExtension.class})
@PactTestFor(
    providerName = "userProducerKafka", 
    providerType = ProviderType.ASYNCH,
    pactVersion = PactSpecVersion.V3
)
public class UserListenerContractTest {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String KEY_CONTENT_TYPE = "contentType";

    @InjectMocks
    private UserConsumer consumer;

    @Mock
    private UserConsumerService service;

    @Pact(consumer = "recommendationConsumerKafka")
    public MessagePact validUserMessageFromKafkaProvider(MessagePactBuilder builder) { 
        PactDslJsonBody jsonBody = new PactDslJsonBody();

        jsonBody.object("subject")
                .integerType("id", 1)
                .integerType("userId", 123)
                .stringType("firstName", "John")
                .stringType("lastName", "Doe")
                .stringType("email", "john.doe@example.com")
                .date("createdDate", "yyyy-MM-dd")
                .stringType("createdBy", "test-user")
                .stringType("lastModifiedBy", "test-user")
                .date("lastModifiedDate", "yyyy-MM-dd")
                .closeObject()
            .asBody()
            .stringValue("eventType", "USER_ADDED")
            .date("createdDate", "yyyy-MM-dd")
            .stringValue("createdBy", "test-user");

        return builder
            .expectsToReceive("userProducerKafka")
            .withMetadata(Map.of(JSON_CONTENT_TYPE, KEY_CONTENT_TYPE))
            .withContent(jsonBody)
            .toPact();
    }
    
    @Test
    @PactTestFor(pactMethod = "validUserMessageFromKafkaProvider", providerType = ProviderType.ASYNCH)
    void shouldReceiveValidDomainEventMessage(List<Message> messages) {
        DomainEvent<User> event = new DomainEvent<>();
        User user = new User()
            .id(1L)
            .email("john.doe@example.com")
            .userId(123L)
            .firstName("John")
            .lastName("Doe")
            .createdDate(Date.from(Instant.now()))
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(Date.from(Instant.now()));

        event.setSubject(user);
        event.setEventType(UserEventType.USER_ADDED);
        event.setCreatedBy("test-user");
        event.setCreatedDate(Date.from(Instant.now()));

        // when(service.apply(any(DomainEvent.class))).thenReturn(Mono.just(user));
        
        assertNotNull(messages);
        var mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructParametricType(DomainEvent.class, User.class);
        
        messages.forEach(message -> { 
            assertDoesNotThrow(() -> consumer.consume(
            
                new ObjectMapper().readValue(message.contentsAsString(), type)));
                verify(service, times(1)).apply(event);
        });
        
    }
}
