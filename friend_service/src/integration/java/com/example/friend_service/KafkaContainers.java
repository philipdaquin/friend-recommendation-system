package com.example.friend_service;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaContainers {
    private static final String IMAGE_NAME = "confluentinc/cp-kafka:latest";
    
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse(IMAGE_NAME))
        .withReuse(true);

}
