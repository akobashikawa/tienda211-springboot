package com.example.tienda211.ventas.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.tienda211.infrastructure.NatsEventPublisher;
import com.example.tienda211.personas.application.PersonaService;
import com.example.tienda211.personas.domain.Persona;
import com.example.tienda211.productos.application.ProductoService;
import com.example.tienda211.productos.domain.Producto;
import com.example.tienda211.ventas.domain.Venta;
import com.example.tienda211.ventas.domain.VentaRepository;

class VentaServiceTest {

	@Mock
	private VentaRepository ventaRepository;

	@Mock
	private ProductoService productoService;

	@Mock
	private PersonaService personaService;

	@Mock
	private NatsEventPublisher eventPublisher;

	@InjectMocks
	private VentaService ventaService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateItem() {
		// Datos de prueba
		Producto producto = new Producto(1L, "Producto 1");
		Persona persona = new Persona(1L, "John Doe");
		VentaDTO ventaDTO = new VentaDTO();
		ventaDTO.setPersona_id(1L);
		ventaDTO.setProducto_id(1L);
		ventaDTO.setPrecio(new BigDecimal("100.0"));
		ventaDTO.setCantidad(2);

		// Mockear los servicios dependientes
		when(productoService.getItemById(1L)).thenReturn(Optional.of(producto));
		when(personaService.getItemById(1L)).thenReturn(Optional.of(persona));

		Venta venta = new Venta();
		venta.setProducto(producto);
		venta.setPersona(persona);
		venta.setPrecio(new BigDecimal("100.0"));
		venta.setCantidad(2);
		venta.setFechaHora(LocalDateTime.now());

		when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

		// Actuar
		Venta newItem = ventaService.createItem(ventaDTO);

		// Aserciones
		assertNotNull(newItem);
		assertEquals(producto, newItem.getProducto());
		assertEquals(persona, newItem.getPersona());

		// Verificar que los métodos dependientes fueron llamados
		verify(productoService).getItemById(1L);
		verify(personaService).getItemById(1L);
		verify(ventaRepository).save(any(Venta.class));

		// Capturar y verificar el evento publicado
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(eventPublisher).publishEvent(eq("venta.created"), captor.capture());

		Map<String, Object> payload = captor.getValue();
		assertEquals(newItem, payload.get("venta"));
		assertEquals(producto, payload.get("producto"));
	}

	@Test
	void testUpdateItem() {
		// Datos de prueba
		Producto producto = new Producto(1L, "Producto 1");
		Persona persona = new Persona(1L, "John Doe");
		VentaDTO ventaDTO = new VentaDTO();
		ventaDTO.setPersona_id(1L);
		ventaDTO.setProducto_id(1L);
		ventaDTO.setPrecio(new BigDecimal("150.0"));
		ventaDTO.setCantidad(3);

		Venta venta = new Venta();
		venta.setId(1L);
		venta.setProducto(producto);
		venta.setPersona(persona);
		venta.setPrecio(new BigDecimal("100.0"));
		venta.setCantidad(2);

		// Mockear las dependencias
		when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
		when(productoService.getItemById(1L)).thenReturn(Optional.of(producto));
		when(personaService.getItemById(1L)).thenReturn(Optional.of(persona));
		when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

		// Actuar
		Venta updatedItem = ventaService.updateItem(1L, ventaDTO);

		// Aserciones
		assertNotNull(updatedItem);
		assertEquals(new BigDecimal("150.0"), updatedItem.getPrecio());
		assertEquals(3, updatedItem.getCantidad());

		// Verificar interacciones con dependencias
		verify(ventaRepository).findById(1L);
		verify(ventaRepository).save(any(Venta.class));

		// Capturar y verificar el evento publicado
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(eventPublisher).publishEvent(eq("venta.updated"), captor.capture());

		Map<String, Object> payload = captor.getValue();
		assertEquals(updatedItem, payload.get("venta"));
		assertEquals(producto, payload.get("producto"));
		assertEquals(2, payload.get("cantidadAnterior")); // Cantidad anterior
	}

	@Test
	void testDeleteItem() {
		// Actuar
		ventaService.deleteItemById(1L);

		// Verificar que se llama al repositorio para eliminar
		verify(ventaRepository, times(1)).deleteById(1L);
	}
}
