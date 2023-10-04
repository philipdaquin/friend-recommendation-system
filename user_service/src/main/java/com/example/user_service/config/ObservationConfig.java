package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

@Configuration
public class ObservationConfig {
    
    // To have the @Observed support 
    @Bean
    ObservedAspect observedAspect(ObservationRegistry registry) { 
        return new ObservedAspect(registry);
    }
}
