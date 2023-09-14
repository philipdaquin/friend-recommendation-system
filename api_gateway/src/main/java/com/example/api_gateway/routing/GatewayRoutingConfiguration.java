package com.example.api_gateway.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutingConfiguration {

    /**
     * 
     * 
     * 
     * @param route is used to create new routes, predicates, and filters to your routes
     *  so that you can route handle based on certain conditions as well as alter the 
     *  request /reponse as you see fit. 
     * 
     * @return
     */
    @Bean
    RouteLocator routingConfig(RouteLocatorBuilder route) { 
        return route.routes()
            .route("friend-service", r -> r
                .path("/friend/**")
                    .filters(filter -> filter)
                        .uri("lb://friend-service"))
            .route("user-service", r -> r
                .path("/user/**")
                    .filters(filter -> filter)
                        .uri("lb://user-service"))
            .route("recommendation-service", r -> r
                .path("/recommendation/**")
                    .filters(filter -> filter)
                        .uri("lb://recommedation-service"))
        .build();
    }
}
