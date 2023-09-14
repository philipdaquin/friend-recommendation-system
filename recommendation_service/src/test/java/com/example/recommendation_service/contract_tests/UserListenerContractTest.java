package com.example.recommendation_service.contract_tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
                .integerType("id", 1L)
                .integerType("userId", 123L)
                .stringType("firstName", "John")
                .stringType("lastName", "Doe")
                .stringType("email", "john.doe@example.com")
                .date("createdDate",  "2000-01-31")
                .stringType("createdBy", "test-user")
                .stringType("lastModifiedBy", "test-user")
                .date("lastModifiedDate", "2000-01-31")
                .closeObject()
            .asBody()
            .stringValue("eventType", "USER_ADDED")
            .date("createdDate", "2000-01-31")
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
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = "2000-01-31";
        
        // Step 1: Parse the date string into a LocalDate
        LocalDate localDate = LocalDate.parse(dateString);
        
        // Step 2: Convert the LocalDate to a Date
        Date newDate = Date.from(localDate.atTime(LocalTime.of(8, 0)).atZone(ZoneId.systemDefault()).toInstant());
        User user = new User()
            .id(1L)
            .email("john.doe@example.com")
            .userId(123L)
            .firstName("John")
            .lastName("Doe")
            .createdDate(newDate)
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(newDate);

        event.setSubject(user);
        event.setEventType(UserEventType.USER_ADDED);
        event.setCreatedBy("test-user");
        event.setCreatedDate(newDate);

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
