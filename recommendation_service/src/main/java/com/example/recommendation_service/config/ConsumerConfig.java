package com.example.recommendation_service.config;

import java.time.Clock;
import java.util.Collections;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.retrytopic.DefaultDestinationTopicResolver;
import org.springframework.kafka.retrytopic.DestinationTopicResolver;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.kafka.retrytopic.RetryTopicBeanNames;


@Configuration
@EnableKafka
public class ConsumerConfig {

    private final KafkaTemplate<Object, Object> broker;

    private final ConsumerFactory<String, Object> consumerFactory;

    public ConsumerConfig(KafkaTemplate<Object, Object> broker, ConsumerFactory<String, Object> factory) { 
        this.broker = broker;
        this.consumerFactory = factory;
    }

    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaBlockingRetry() { 
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(retryError());
        consumerFactory.getListeners();

        return factory;
    }

    public DefaultErrorHandler retryError() { 
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(broker);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000, 3));
    }

    @Bean(name = RetryTopicBeanNames.DESTINATION_TOPIC_RESOLVER_BEAN_NAME)
    public DestinationTopicResolver topicResolver(ApplicationContext context) { 
        DefaultDestinationTopicResolver resolver = new DefaultDestinationTopicResolver(Clock.systemUTC());
        resolver.defaultFalse();
        resolver.setClassifications(Collections.emptyMap(), true);
        return resolver;
    }
}
