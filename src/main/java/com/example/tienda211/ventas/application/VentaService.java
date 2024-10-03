package com.example.tienda211.ventas.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.tienda211.application.GenericEvent;
import com.example.tienda211.infrastructure.NatsEventPublisher;
import com.example.tienda211.personas.application.PersonaService;
import com.example.tienda211.personas.domain.Persona;
import com.example.tienda211.productos.application.ProductoService;
import com.example.tienda211.productos.domain.Producto;
import com.example.tienda211.ventas.domain.Venta;
import com.example.tienda211.ventas.domain.VentaRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VentaService {

	private final VentaRepository ventaRepository;
	
	private final ProductoService productoService;
	private final PersonaService personaService;
	private final NatsEventPublisher eventPublisher;
//	private ApplicationEventPublisher eventPublisher;

//	public VentaService(VentaReposupdatedItemository, ProductoService productoService, PersonaService personaService,
//			ApplicationEventPublisher eventPublisher) {
	public VentaService(VentaRepository ventaRepository, ProductoService productoService, PersonaService personaService, NatsEventPublisher eventPublisher) {
		this.ventaRepository = ventaRepository;
		this.productoService = productoService;
		this.personaService = personaService;
		this.eventPublisher = eventPublisher;
	}

	public List<Venta> getItems() {
		return ventaRepository.findAll();
	}

	public Venta getItemById(Long id) {
		return ventaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Venta no encontrada: " + id));
	}

	public Venta createItem(VentaDTO ventaDTO) {
		Venta venta = new Venta();
		Producto producto = productoService.getItemById(ventaDTO.getProducto_id());
		Persona persona = personaService.getItemById(ventaDTO.getPersona_id());

		venta.setProducto(producto);
		venta.setPersona(persona);
		venta.setPrecio(ventaDTO.getPrecio());
		venta.setCantidad(ventaDTO.getCantidad());
		venta.setFechaHora(LocalDateTime.now());

		Venta newItem = ventaRepository.save(venta);
		Map<String, Object> payload = new HashMap<>();
		payload.put("ventaId", newItem.getId());
		payload.put("productoId", producto.getId());
		payload.put("cantidad", ventaDTO.getCantidad());
		payload.put("precio", ventaDTO.getPrecio());
//    	eventPublisher.publishEvent(new GenericEvent(this, "ventaCreate", payload));
		eventPublisher.publishEvent("venta.created", payload);
		return newItem;
	}

	public Venta updateItem(Long id, VentaDTO ventaDTO) {
		Venta found = ventaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Venta no encontrada"));
		
		Producto producto = productoService.getItemById(ventaDTO.getProducto_id());
		Persona persona = personaService.getItemById(ventaDTO.getPersona_id());

		found.setProducto(producto);
		found.setPersona(persona);

		if (ventaDTO.getPrecio() != null) {
			found.setPrecio(ventaDTO.getPrecio());
		}

		int cantidadAnterior = found.getCantidad();
		if (ventaDTO.getCantidad() != null) {
			found.setCantidad(ventaDTO.getCantidad());
		}
		found.setFechaHora(LocalDateTime.now());

		Venta updatedItem = ventaRepository.save(found);
		
		Map<String, Object> payload = new HashMap<>();
		payload.put("ventaId", updatedItem.getId());
		payload.put("productoId", producto.getId());
		payload.put("cantidad", ventaDTO.getCantidad());
		payload.put("precio", ventaDTO.getPrecio());
		payload.put("cantidadAnterior", cantidadAnterior);
//		eventPublisher.publishEvent(new GenericEvent(this, "ventaUpdate", payload));
		System.out.println("updatedItem" + updatedItem);
		System.out.println("payload" + payload);
		eventPublisher.publishEvent("venta.updated", payload);
		
		return updatedItem;
	}

	public void deleteItemById(Long id) {
		if (!ventaRepository.existsById(id)) {
	        throw new RuntimeException("Venta con ID " + id + " no encontrada");
	    }
		ventaRepository.deleteById(id);
	}

}
