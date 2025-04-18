package ru.practicum.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class StatsClientConfig {

    @Bean
    public RestClient restClient(RestTemplateBuilder builder) {
        return RestClient.builder()
                .baseUrl("http://stats-server:9090")
                .build();
    }
}
