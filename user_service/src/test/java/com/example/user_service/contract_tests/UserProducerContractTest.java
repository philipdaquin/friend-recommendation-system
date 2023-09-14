package com.example.user_service.contract_tests;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.user_service.domains.User;
import com.example.user_service.domains.events.DomainEvent;
import com.example.user_service.domains.events.EventType;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;

@Provider("userProducerKafka")
@Consumer("recommendationConsumerKafka")
@PactBroker(url = "http://localhost:9292")
public class UserProducerContractTest {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String KEY_CONTENT_TYPE = "contentType";

    @BeforeEach
    void setup(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerfificationTestTemplate(PactVerificationContext context) { 
        context.verifyInteraction();
    }

    @PactVerifyProvider(value = "userProducerKafka")
    MessageAndMetadata verifyMessage() { 
        User user = new User()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .createdDate(Date.from(Instant.now()))
            .createdBy("test-user")
            .lastModifiedBy("test-user")
            .lastModifiedDate(Date.from(Instant.now()));

        DomainEvent<User> event = new DomainEvent<User>();
        event.setCreatedBy("test-user");
        event.setCreatedDate(Date.from(Instant.now()));
        event.setSubject(user);
        event.setEventType(EventType.USER_ADDED);

        JsonSerializer<DomainEvent<User>> serializer = new JsonSerializer();
        
        return new MessageAndMetadata(
            serializer.serialize("user", event), 
            Map.of(KEY_CONTENT_TYPE, JSON_CONTENT_TYPE)
        );
    }
}
