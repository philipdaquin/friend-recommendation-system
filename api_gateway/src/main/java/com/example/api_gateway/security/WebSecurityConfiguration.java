package com.example.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfiguration {
    
    @Bean
    SecurityWebFilterChain actuatorHttpSecurity(ServerHttpSecurity http) { 
        return http
            .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/actuator/**"))
            .headers(header -> header.hsts(hst -> hst.disable()))
            .authorizeExchange(exchange -> exchange.anyExchange().permitAll())
        .build();
    }

    // @Bean
    // SecurityWebFilterChain secureHttpApi(ServerHttpSecurity http) { 
    //     return http
    //         .headers(header -> header.hsts(hts -> hts.disable()))
    //         .authorizeExchange(exchange -> exchange.anyExchange().authenticated())
    //         // .oauth2ResourceServer(OAuth2ResourceServerSpec::jwt)
    //         .csrf(csrf -> csrf.disable())
    //     .build();
    // }

}
