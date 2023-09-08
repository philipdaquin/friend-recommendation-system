package com.example.api_gateway.fallback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    
    @GetMapping(path = "/user-service-fallback")
    public String userServiceFallback() { 
        return "User Service Fallback";
    }

    @GetMapping(path = "/friend-service-fallback")
    public String friendServiceFallback() { 
        return "friend Service Fallback";
    }

    @GetMapping(path = "/recommendation-service-fallback")
    public String recommendationServiceFallback() { 
        return "Recommendation Service Fallback";
    }
}
