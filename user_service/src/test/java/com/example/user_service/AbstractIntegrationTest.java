package com.example.user_service;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;


import jakarta.validation.constraints.NotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest( classes = UserServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Intializer.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));


    @ClassRule
    public static GenericContainer<?> postgres = new GenericContainer<>(DockerImageName.parse("postgres:latest"))
        .withExposedPorts(5432)
        .withEnv("POSTGRES_PASSWORD", "password")
        .withEnv("POSTGRES_USER", "postgres");

    static { postgres.start(); }

    public static class Intializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            // TODO Auto-generated method stub
            String dbUrl = String.format(
                "jdbc:postgresql://%s:%d/%s", 
                postgres.getHost(), 
                postgres.getMappedPort(5432),
                "postgres"  
            );
            TestPropertyValues values = TestPropertyValues.of(
                    "postgres.host=" + postgres.getHost(),
                    "postgres.port=" + postgres.getMappedPort(5432),
                    "postgres.url=" + dbUrl,
                    "spring.datasource.url=" + dbUrl,
                    "spring.cloud.stream.kafka.binder.brokers=" + kafka.getBootstrapServers(),
                    "eureka.client.enabled=false");
            values.applyTo(applicationContext);

        }   

    }
}