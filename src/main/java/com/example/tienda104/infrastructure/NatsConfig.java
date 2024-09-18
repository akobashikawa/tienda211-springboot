package com.example.tienda104.infrastructure;

import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NatsConfig {

    @Bean
    Connection natsConnection() throws Exception {
        return Nats.connect("nats://localhost:4222");
    }
}

