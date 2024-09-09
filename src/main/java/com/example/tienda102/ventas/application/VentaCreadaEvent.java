package com.example.tienda102.ventas.application;

import org.springframework.context.ApplicationEvent;

import com.example.tienda102.ventas.domain.Venta;

public class VentaCreadaEvent extends ApplicationEvent {
    
	private static final long serialVersionUID = 1L;
	
	private final Venta venta;

    public VentaCreadaEvent(Object source, Venta venta) {
        super(source);
        this.venta = venta;
    }

    public Venta getVenta() {
        return venta;
    }
}
