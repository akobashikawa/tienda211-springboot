package com.example.tienda211.ventas.application;

import org.springframework.context.ApplicationEvent;

import com.example.tienda211.productos.domain.Producto;
import com.example.tienda211.ventas.domain.Venta;

public class VentaCreateEvent extends ApplicationEvent {
    
	private static final long serialVersionUID = 1L;
	
	private Venta venta;

	private Producto producto;

    public VentaCreateEvent(Object source, Venta venta, Producto producto) {
        super(source);
        this.venta = venta;
        this.producto = producto;
    }

    public Venta getVenta() {
    	return venta;
    }
    
    public Producto getProducto() {
        return producto;
    }
}
