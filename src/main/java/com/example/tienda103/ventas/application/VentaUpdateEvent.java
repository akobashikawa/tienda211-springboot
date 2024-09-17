package com.example.tienda103.ventas.application;

import org.springframework.context.ApplicationEvent;

import com.example.tienda103.productos.domain.Producto;
import com.example.tienda103.ventas.domain.Venta;

public class VentaUpdateEvent extends ApplicationEvent {
    
	private static final long serialVersionUID = 1L;
	
	private Venta venta;

	private Producto producto;

	private int cantidadAnterior;

    public VentaUpdateEvent(Object source, Venta venta, Producto producto, int cantidadAnterior) {
        super(source);
        this.venta = venta;
        this.producto = producto;
        this.cantidadAnterior = cantidadAnterior;
    }

    public Venta getVenta() {
    	return venta;
    }
    
    public Producto getProducto() {
    	return producto;
    }
    
    public int getCantidadAnterior() {
        return cantidadAnterior;
    }
}
