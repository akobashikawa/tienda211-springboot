package com.example.tienda104.ventas.application;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

import com.example.tienda104.infrastructure.SocketIOService;
import com.example.tienda104.ventas.domain.Venta;
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
public class VentaNatsEventListener {

    @Autowired
    private Connection natsConnection;

    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private SocketIOService socketIOService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @PostConstruct
    public void init() throws Exception {
        subscribeToEvent("venta.created", this::handleVentaCreateEvent);
        subscribeToEvent("venta.updated", this::handleVentaUpdateEvent);
    }
    
    private void subscribeToEvent(String topic, MessageHandler handler) throws Exception {
        Dispatcher dispatcher = natsConnection.createDispatcher(handler);
        dispatcher.subscribe(topic);
    }
    
    private Map<String, Object> getPayload(Message msg) throws JsonMappingException, JsonProcessingException {
    	String json = new String(msg.getData());
        return objectMapper.readValue(json, Map.class);
    }

    private void handleVentaCreateEvent(Message msg) {
        try {
            Map<String, Object> payload = getPayload(msg);
            Venta venta = objectMapper.convertValue(payload.get("venta"), Venta.class);
            socketIOService.emitItem("ventaCreated", venta);
            System.out.println("Venta creada: " + venta.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleVentaUpdateEvent(Message msg) {
        try {
        	Map<String, Object> payload = getPayload(msg);
            Venta venta = objectMapper.convertValue(payload.get("venta"), Venta.class);
            socketIOService.emitItem("ventaUpdated", venta);
            System.out.println("Venta actualizada: " + venta.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

