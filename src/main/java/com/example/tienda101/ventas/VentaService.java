package com.example.tienda101.ventas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tienda101.personas.Persona;
import com.example.tienda101.personas.PersonaRepository;
import com.example.tienda101.productos.Producto;
import com.example.tienda101.productos.ProductoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PersonaRepository personaRepository;

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
