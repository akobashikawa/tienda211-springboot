package com.example.tienda104.infrastructure;

import io.nats.client.Connection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Service;

@Service
public class NatsEventPublisher {

	private final Connection natsConnection;
	private final ObjectMapper objectMapper;

	public NatsEventPublisher(Connection natsConnection) {
		this.natsConnection = natsConnection;
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	public void publishEvent(String subject, Object payload) {
		try {
			String message = objectMapper.writeValueAsString(payload);
			natsConnection.publish(subject, message.getBytes());
			System.out.println("Evento NATS publicado " + subject + ": " + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
