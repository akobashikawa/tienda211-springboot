package com.example.tienda102.productos.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.tienda102.gateway.application.GenericEvent;
import com.example.tienda102.productos.domain.Producto;
import com.example.tienda102.ventas.application.VentaCreateEvent;
import com.example.tienda102.ventas.application.VentaDTO;
import com.example.tienda102.ventas.application.VentaUpdateEvent;
import com.example.tienda102.ventas.domain.Venta;

@Component
public class GenericEventListener {

	private final ProductoService productoService;

	public GenericEventListener(ProductoService productoService) {
		this.productoService = productoService;
	}

	@EventListener
	public void onGenericEvent(GenericEvent event) {
		String eventType = event.getEventType();
		
		switch (eventType) {
			case "ventaCreate":
				handleVentaCreate(event);
				break;
			case "ventaUpdate":
				handleVentaUpdate(event);
				break;
			default:
	            System.out.println("Tipo de evento no reconocido: " + eventType);
		}
	}
	
	private void handleVentaCreate(GenericEvent event) {
		Venta venta = (Venta) event.getPayload().get("venta");
		Producto producto = (Producto) event.getPayload().get("producto");
		System.out.println("Crear nueva venta para el producto " + producto.getId() + ": " + producto.getNombre());
		productoService.decProductoCantidad(producto, venta.getCantidad());
	}
	
	private void handleVentaUpdate(GenericEvent event) {
		Venta venta = (Venta) event.getPayload().get("venta");
		Producto producto = (Producto) event.getPayload().get("producto");
		int cantidadAnterior = (int) event.getPayload().get("cantidadAnterior");
		System.out.println("Actualizar venta " + venta.getId() + " para el producto " + producto.getId() + ": "
				+ producto.getNombre());
		productoService.decProductoCantidad(producto, venta.getCantidad(), cantidadAnterior);
	}

}
