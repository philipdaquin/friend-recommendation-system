package com.example.recommendation_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

@Configuration
@Profile({"docker, kubernetes"})
public class ObservationConfig {
    
    // To have the @Observed support 
    @Bean
    ObservedAspect observedAspect(ObservationRegistry registry) { 
        return new ObservedAspect(registry);
    }
}
