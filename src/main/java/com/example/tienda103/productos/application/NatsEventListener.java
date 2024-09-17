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

	@PostConstruct
	public void subscribeToVentaCreateEvent() throws Exception {
		Dispatcher dispatcher = natsConnection.createDispatcher((Message msg) -> {
			try {
				String ventaCreateJson = new String(msg.getData());
				// Deserializar el JSON para obtener la venta y el producto
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());
				objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

				// Asegúrate de que el JSON tenga un mapa con claves "venta" y "producto"
				Map<String, Object> payload = objectMapper.readValue(ventaCreateJson, Map.class);

				// Convertir el mapa en los objetos correspondientes
				Venta venta = objectMapper.convertValue(payload.get("venta"), Venta.class);
				Producto producto = objectMapper.convertValue(payload.get("producto"), Producto.class);

				// Llamar al método del servicio de productos para actualizar la cantidad
				productoService.decProductoCantidad(producto, venta.getCantidad());

				System.out.println("Producto actualizado para venta " + venta.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		dispatcher.subscribe("venta.create");
	}
	
	@PostConstruct
	public void subscribeToVentaUpdateEvent() throws Exception {
		Dispatcher dispatcher = natsConnection.createDispatcher((Message msg) -> {
			try {
				String ventaCreateJson = new String(msg.getData());
				// Deserializar el JSON para obtener la venta y el producto
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());
				objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

				// Asegúrate de que el JSON tenga un mapa con claves "venta" y "producto"
				Map<String, Object> payload = objectMapper.readValue(ventaCreateJson, Map.class);

				// Convertir el mapa en los objetos correspondientes
				Venta venta = objectMapper.convertValue(payload.get("venta"), Venta.class);
				Producto producto = objectMapper.convertValue(payload.get("producto"), Producto.class);
				int cantidadAnterior = (int) payload.get("cantidadAnterior");

				// Llamar al método del servicio de productos para actualizar la cantidad
				productoService.decProductoCantidad(producto, venta.getCantidad(), cantidadAnterior);

				System.out.println("Producto actualizado para venta " + venta.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		dispatcher.subscribe("venta.update");
	}
}
