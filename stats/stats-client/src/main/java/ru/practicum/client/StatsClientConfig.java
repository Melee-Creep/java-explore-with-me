package ru.practicum.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class StatsClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
//                .baseUrl("http://localhost:9090")
                .baseUrl("http://stats-server:9090")
                .build();
    }
}

