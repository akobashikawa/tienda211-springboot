package com.example.tienda211.infrastructure;

import io.nats.client.Connection;
import io.nats.client.Nats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NatsConfig {

    @Bean
    Connection natsConnection(@Value("${nats.url}") String host) throws Exception {
        return Nats.connect(host);
    }
}

