package com.example.friend_service.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.example.friend_service.config.KafkaProducerConfig;
import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.events.DomainEvent;
import com.example.friend_service.domains.events.EventType;
import com.example.friend_service.service.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *  Testing ERror
 * 
 */

// @EmbeddedKafka
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmbeddedKafkaProducerTest {

    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private EmbeddedKafkaBroker broker;
    
    @Autowired
    private KafkaProducerService producer;


    private static final String TOPIC = "friend";



    /**
        Responsible for the communication between producer and consumers
        
        key-value : String, DomainEvent<Friend>
     
     */
    private BlockingQueue<ConsumerRecord<String, DomainEvent<Friend>>> queue;

    /**
        Allows you to set up a listener a Kafka messages and provides the infrastructure for consuming 
        messages from Kafka Topics
     */
    private KafkaMessageListenerContainer<String, DomainEvent<Friend>> container;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:test");
        registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "root");
        registry.add("spring.datasource.password", () -> "secret");
    }



    @BeforeAll
    void setup() { 
        
        Map<String, Object> configs = new HashMap<>(
            KafkaTestUtils.consumerProps("consumer", "true", broker));
        
        DefaultKafkaConsumerFactory<String, DomainEvent<Friend>> consumerFactory = new DefaultKafkaConsumerFactory<>(
        getConsumerProperties());
        
        ContainerProperties containerProperties = new ContainerProperties(TOPIC);
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        
        queue = new LinkedBlockingQueue<>();
        
        container.setupMessageListener((MessageListener<String, DomainEvent<Friend>>) queue::add);
        container.start();

        ContainerTestUtils.waitForAssignment(container, broker.getPartitionsPerTopic());
    }

    private Map<String, Object> getConsumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker.getBrokersAsString(),
                ConsumerConfig.GROUP_ID_CONFIG, "consumer",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @Test
    void shouldSendMessageToKafka() throws InterruptedException, JsonProcessingException { 
        
        Long userId = 1L;

        // Create a Domain Event 
        DomainEvent<Friend> event = new DomainEvent<>();
        Friend friend = new Friend(userId, 2L); 
        event.setSubject(friend);
        event.setEventType(EventType.FRIEND_ADDED);
        // Send to the kafka 
        producer.send(event);        

        // Read the message with a test consumer from Kafka and assert its properties
        ConsumerRecord<String, DomainEvent<Friend>> messages = queue.poll(500, TimeUnit.MILLISECONDS);
        
        

        assertNotNull(messages);
        // assertEquals("friend", messages.key());

        // DomainEvent<Friend> result = mapper.readValue(messages.value(), new TypeReference<DomainEvent<Friend>>() {});
        // assertNotNull(result);
        // assertEquals(friend, result.getSubject());
        // assertEquals(userId, result.getSubject().getUserId());

    }


    @AfterAll
    void shutDown() { 
        container.stop();
    }

}
