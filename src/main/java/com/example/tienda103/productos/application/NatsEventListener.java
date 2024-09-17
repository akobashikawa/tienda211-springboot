package com.example.tienda103.productos.application;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;

import com.example.tienda103.productos.domain.Producto;
import com.example.tienda103.ventas.domain.Venta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class NatsEventListener {

    @Autowired
    private Connection natsConnection;

    @Autowired
    private ProductoService productoService;

    private final ObjectMapper objectMapper;

    public NatsEventListener() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @PostConstruct
    public void init() throws Exception {
        subscribeToVentaCreateEvent();
        subscribeToVentaUpdateEvent();
    }

    public void subscribeToVentaCreateEvent() throws Exception {
        Dispatcher dispatcher = natsConnection.createDispatcher(this::handleVentaCreateEvent);
        dispatcher.subscribe("venta.create");
    }

    public void subscribeToVentaUpdateEvent() throws Exception {
        Dispatcher dispatcher = natsConnection.createDispatcher(this::handleVentaUpdateEvent);
        dispatcher.subscribe("venta.update");
    }

    private void handleVentaCreateEvent(Message msg) {
        try {
            String ventaCreateJson = new String(msg.getData());
            Map<String, Object> payload = objectMapper.readValue(ventaCreateJson, Map.class);
            Venta venta = objectMapper.convertValue(payload.get("venta"), Venta.class);
            Producto producto = objectMapper.convertValue(payload.get("producto"), Producto.class);

            productoService.decProductoCantidad(producto, venta.getCantidad());

            System.out.println("Producto actualizado para venta " + venta.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleVentaUpdateEvent(Message msg) {
        try {
            String ventaUpdateJson = new String(msg.getData());
            Map<String, Object> payload = objectMapper.readValue(ventaUpdateJson, Map.class);
            Venta venta = objectMapper.convertValue(payload.get("venta"), Venta.class);
            Producto producto = objectMapper.convertValue(payload.get("producto"), Producto.class);
            int cantidadAnterior = (int) payload.get("cantidadAnterior");

            productoService.decProductoCantidad(producto, venta.getCantidad(), cantidadAnterior);

            System.out.println("Producto actualizado para venta " + venta.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

