package com.example.tienda101.ventas.application;

import org.springframework.stereotype.Service;

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
    
    public Venta createItem(VentaDTO ventaDTO) {
    	Producto producto = productoRepository.findById(ventaDTO.getProducto_id())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Persona persona = personaRepository.findById(ventaDTO.getPersona_id())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setPersona(persona);
        venta.setPrecio(ventaDTO.getPrecio());
        venta.setCantidad(ventaDTO.getCantidad());
        venta.setFecha(LocalDate.now());

        return ventaRepository.save(venta);
    }

    public Venta updateItem(Long id, VentaDTO ventaDTO) {
    	ventaDTO.setId(id);
    	
    	Producto producto = productoRepository.findById(ventaDTO.getProducto_id())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Persona persona = personaRepository.findById(ventaDTO.getPersona_id())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setPersona(persona);
        venta.setPrecio(ventaDTO.getPrecio());
        venta.setCantidad(ventaDTO.getCantidad());
        venta.setFecha(LocalDate.now());

        return ventaRepository.save(venta);
    }

    public void deleteItemById(Long id) {
        ventaRepository.deleteById(id);
    }
}
