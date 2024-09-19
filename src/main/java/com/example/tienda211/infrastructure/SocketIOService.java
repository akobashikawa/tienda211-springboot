package com.example.tienda211.infrastructure;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.example.tienda211.productos.domain.Producto;
import com.example.tienda211.ventas.domain.Venta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SocketIOService {

	private SocketIOServer server = null;
	private final ObjectMapper objectMapper;

	public SocketIOService(@Value("${socketio.host}") String host, @Value("${socketio.port}") int port) {
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port);
		config.setOrigin("*");

		// Configurar el ObjectMapper con soporte para Java 8 Date/Time API
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // Módulo para manejar fechas como LocalDateTime
		objectMapper.findAndRegisterModules(); // Esto permite registrar automáticamente otros módulos de Jackson

		server = new SocketIOServer(config);

		// Agregar manejadores de eventos
		server.addConnectListener(client -> {
			System.out.println("Cliente conectado: " + client.getSessionId());
		});

		server.addDisconnectListener(client -> {
			System.out.println("Cliente desconectado: " + client.getSessionId());
		});

		// Evento personalizado "productos"
		server.addEventListener("productos", Producto.class, (client, data, ackSender) -> {
			System.out.println("Producto recibido: " + data);
			// Emitir mensaje de vuelta a los clientes
			server.getBroadcastOperations().sendEvent("productos", data);
		});

		server.start();

		System.out.println("SocketIOServer iniciado: " + server.toString());

	}

	@PreDestroy
	public void stopServer() {
		server.stop();
	}

	public void emitItem(String subject, Object item) {
		try {
			// Serializar el objeto a JSON y luego emitir
			String json = objectMapper.writeValueAsString(item);
			server.getBroadcastOperations().sendEvent(subject, json);
			System.out.println("Evento SocketIO enviado: " + subject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}