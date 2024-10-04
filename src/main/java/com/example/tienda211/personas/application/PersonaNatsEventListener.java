package com.example.tienda211.personas.application;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

import com.example.tienda211.infrastructure.SocketIOService;
import com.example.tienda211.personas.domain.Persona;
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
            int personaId = Integer.parseInt((String) payload.get("personaId"));
            socketIOService.emitItem("personaCreated", personaId);
            System.out.println("persona.created: " + personaId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePersonaUpdateEvent(Message msg) {
        try {
        	Map<String, Object> payload = getPayload(msg);
        	int personaId = Integer.parseInt((String) payload.get("personaId"));
            socketIOService.emitItem("personaUpdated", personaId);
            System.out.println("persona.updated: " + personaId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

