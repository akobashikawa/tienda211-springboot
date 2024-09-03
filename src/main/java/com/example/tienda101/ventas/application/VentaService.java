package com.example.tienda101.ventas.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tienda101.personas.domain.Persona;
import com.example.tienda101.personas.domain.PersonaRepository;
import com.example.tienda101.productos.domain.Producto;
import com.example.tienda101.productos.domain.ProductoRepository;
import com.example.tienda101.ventas.domain.Venta;
import com.example.tienda101.ventas.domain.VentaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class VentaService {

	private final VentaRepository ventaRepository;
    
    private final ProductoRepository productoRepository;

    private final PersonaRepository personaRepository;
    
    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository, PersonaRepository personaRepository) {
        this.ventaRepository = ventaRepository;
		this.productoRepository = productoRepository;
		this.personaRepository = personaRepository;
    }

    public List<Venta> getItems() {
        return ventaRepository.findAll();
    }

    public Venta getItemById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }
    
    public Venta createItem(Venta venta) {
    	return this.saveItem(venta);
    }

    public Venta updateItem(Long id, Venta venta) {
    	venta.setId(id);
        return this.saveItem(venta);
    }
    
    @Transactional
    public Venta saveItem(Venta venta) {
    	Producto producto = productoRepository.findById(venta.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Persona persona = personaRepository.findById(venta.getPersona().getId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
        venta.setProducto(producto);
        venta.setPersona(persona);
        venta.setFecha(LocalDate.now());
        
        Venta savedVenta = ventaRepository.save(venta);
        
        decreaseProductQuantity(producto, venta.getCantidad());

        return savedVenta;
    }

    public void deleteItemById(Long id) {
        ventaRepository.deleteById(id);
    }
    
    private void decreaseProductQuantity(Producto producto, int quantity) {
        if (producto.getCantidad() < quantity) {
            throw new RuntimeException("Cantidad insuficiente para el producto " + producto.getId());
        }
        producto.setCantidad(producto.getCantidad() - quantity);
        productoRepository.save(producto);
    }
}
