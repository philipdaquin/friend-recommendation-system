package com.example.api_gateway.fallback;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    
    @GetMapping(path = "/user-service-fallback")
    public ResponseEntity<String> userService() { 
        return new ResponseEntity<>("We are sorry, but user service is currently out of service. \nPlease try later",
                HttpStatusCode.valueOf(503));
    }

    @GetMapping(path = "/friend-service-fallback")
    public ResponseEntity<String> friendService() { 
        return new ResponseEntity<>("We are sorry, but friend service is currently out of service. \nPlease try later",
                HttpStatusCode.valueOf(503));
    }

    @GetMapping(path = "/recommendation-service-fallback")
    public ResponseEntity<String> recommendationService() { 
        return new ResponseEntity<>("We are sorry, but recommendation service is currently out of service. \nPlease try later",
                HttpStatusCode.valueOf(503));
    }
}
