package com.example.tienda104.personas.application;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

import com.example.tienda104.infrastructure.SocketIOService;
import com.example.tienda104.personas.domain.Persona;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class PersonaNatsEventListener {

    @Autowired
    private Connection natsConnection;

    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private SocketIOService socketIOService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @PostConstruct
    public void init() throws Exception {
        subscribeToEvent("persona.created", this::handlePersonaCreateEvent);
        subscribeToEvent("persona.updated", this::handlePersonaUpdateEvent);
    }
    
    private void subscribeToEvent(String topic, MessageHandler handler) throws Exception {
        Dispatcher dispatcher = natsConnection.createDispatcher(handler);
        dispatcher.subscribe(topic);
    }
    
    private Map<String, Object> getPayload(Message msg) throws JsonMappingException, JsonProcessingException {
    	String json = new String(msg.getData());
        return objectMapper.readValue(json, Map.class);
    }

    private void handlePersonaCreateEvent(Message msg) {
        try {
            Map<String, Object> payload = getPayload(msg);
            Persona persona = objectMapper.convertValue(payload.get("persona"), Persona.class);
            socketIOService.emitItem("personaCreated", persona);
            System.out.println("persona.created: " + persona.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePersonaUpdateEvent(Message msg) {
        try {
        	Map<String, Object> payload = getPayload(msg);
            Persona persona = objectMapper.convertValue(payload.get("persona"), Persona.class);
            socketIOService.emitItem("personaUpdated", persona);
            System.out.println("persona.updated: " + persona.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

