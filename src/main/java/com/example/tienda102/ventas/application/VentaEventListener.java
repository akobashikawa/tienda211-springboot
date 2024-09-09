package com.example.tienda102.ventas.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.tienda102.ventas.domain.Venta;

@Component
public class VentaEventListener {

    @EventListener
    public void onVentaCreada(VentaCreadaEvent event) {
        Venta venta = event.getVenta();
        // Realiza alguna acción cuando se crea una venta
        System.out.println("Nueva venta creada: " + venta.getId() + " para el producto " + venta.getProducto().getNombre());
    }
}
