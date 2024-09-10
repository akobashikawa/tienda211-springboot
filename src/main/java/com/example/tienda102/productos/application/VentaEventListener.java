package com.example.tienda102.productos.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.tienda102.productos.domain.Producto;
import com.example.tienda102.ventas.application.VentaCreateEvent;
import com.example.tienda102.ventas.application.VentaDTO;
import com.example.tienda102.ventas.application.VentaUpdateEvent;
import com.example.tienda102.ventas.domain.Venta;

@Component
public class VentaEventListener {

	private final ProductoService productoService;

	public VentaEventListener(ProductoService productoService) {
		this.productoService = productoService;
	}

	@EventListener
	public void onVentaCreate(VentaCreateEvent event) {
		Venta venta = event.getVenta();
		Producto producto = event.getProducto();
		// Realiza alguna acción cuando se crea una venta
		System.out.println(
				"Crear nueva venta para el producto " + producto.getId() + ": " + producto.getNombre());
		productoService.decProductoCantidad(producto, venta.getCantidad());
	}
	
	@EventListener
	public void onVentaUpdate(VentaUpdateEvent event) {
		Venta venta = event.getVenta();
		Producto producto = event.getProducto();
		int cantidadAnterior = event.getCantidadAnterior();
		// Realiza alguna acción cuando se actualiza una venta
		System.out.println(
				"Actualizar venta " + venta.getId() + " para el producto " + producto.getId() + ": " + producto.getNombre());
		productoService.decProductoCantidad(producto, venta.getCantidad(), cantidadAnterior);
	}
}
