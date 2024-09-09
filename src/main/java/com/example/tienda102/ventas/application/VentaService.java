package com.example.tienda102.ventas.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tienda102.personas.application.PersonaService;
import com.example.tienda102.personas.domain.Persona;
import com.example.tienda102.productos.application.ProductoService;
import com.example.tienda102.productos.domain.Producto;
import com.example.tienda102.ventas.domain.Venta;
import com.example.tienda102.ventas.domain.VentaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

	private final VentaRepository ventaRepository;
	private final ProductoService productoService;
    private final PersonaService personaService;
	private ApplicationEventPublisher eventPublisher;
    
    public VentaService(VentaRepository ventaRepository, ProductoService productoService, PersonaService personaService, ApplicationEventPublisher eventPublisher) {
        this.ventaRepository = ventaRepository;
        this.productoService = productoService;
        this.personaService = personaService;
        this.eventPublisher = eventPublisher;
    }

    public List<Venta> getItems() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> getItemById(Long id) {
        return ventaRepository.findById(id);
    }
    
    public Venta createItem(VentaDTO ventaDTO) {
    	return this.saveItem(ventaDTO);
    }

    public Venta updateItem(Long id, VentaDTO ventaDTO) {
    	ventaDTO.setId(id);
        return this.saveItem(ventaDTO);
    }
    
    @Transactional
    public Venta saveItem(VentaDTO ventaDTO) {
    	Producto producto = productoService.getItemById(ventaDTO.getProducto_id())
    			.orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    	Persona persona = personaService.getItemById(ventaDTO.getPersona_id())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
    	Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setPersona(persona);
        venta.setPrecio(ventaDTO.getPrecio());
        venta.setCantidad(ventaDTO.getCantidad());
        venta.setFechaHora(LocalDateTime.now());
        
        Venta savedVenta = ventaRepository.save(venta);
        
//        decreaseProductQuantity(producto, venta.getCantidad());
        eventPublisher.publishEvent(new VentaCreadaEvent(this, savedVenta));

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
        productoService.updateItem(producto.getId(), producto);
    }
}
