package com.example.friend_service;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class WireMockService {
    

    private static WireMockServer wireMock = new WireMockServer(3000);

    @BeforeAll
    void init() { 


        wireMock.start();

        WireMock.configureFor("localhost", wireMock.port());
    }

    @AfterAll
    public void end() { 

        wireMock.stop();
    }

}
