package com.example.tienda211.productos.application;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

import com.example.tienda211.infrastructure.SocketIOService;
import com.example.tienda211.productos.domain.Producto;
import com.example.tienda211.ventas.domain.Venta;
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
public class ProductoNatsEventListener {

	@Autowired
	private Connection natsConnection;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private SocketIOService socketIOService;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	@PostConstruct
	public void init() throws Exception {
		subscribeToEvent("producto.created", this::handleProductoCreateEvent);
		subscribeToEvent("producto.updated", this::handleProductoUpdateEvent);

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

	private void handleProductoCreateEvent(Message msg) {
		try {
			Map<String, Object> payload = getPayload(msg);
			Producto producto = objectMapper.convertValue(payload.get("producto"), Producto.class);
			socketIOService.emitItem("productoCreated", producto);
			System.out.println("producto.created: " + producto.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleProductoUpdateEvent(Message msg) {
		try {
			Map<String, Object> payload = getPayload(msg);
			Producto producto = objectMapper.convertValue(payload.get("producto"), Producto.class);
			socketIOService.emitItem("productoUpdated", producto);
			System.out.println("producto.updated: " + producto.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleVentaCreateEvent(Message msg) {
		try {
			Map<String, Object> payload = getPayload(msg);
			Venta venta = objectMapper.convertValue(payload.get("venta"), Venta.class);
			Producto producto = objectMapper.convertValue(payload.get("producto"), Producto.class);

			productoService.decProductoCantidad(producto, venta.getCantidad());// además llamará a productoUpdate
			
			System.out.println("venta.created@producto: " + venta.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleVentaUpdateEvent(Message msg) {
		try {
			Map<String, Object> payload = getPayload(msg);
			Venta venta = objectMapper.convertValue(payload.get("venta"), Venta.class);
			Producto producto = objectMapper.convertValue(payload.get("producto"), Producto.class);
			int cantidadAnterior = (int) payload.get("cantidadAnterior");

			productoService.decProductoCantidad(producto, venta.getCantidad(), cantidadAnterior);// además llamará a productoUpdate
			
			System.out.println("venta.updated@producto: " + venta.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
