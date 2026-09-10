package com.pm.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient authWebClient(
            @Value("${auth.service.url}") String authServiceUrl) {

        return WebClient.builder()
                .baseUrl(authServiceUrl)
                .build();
    }
}