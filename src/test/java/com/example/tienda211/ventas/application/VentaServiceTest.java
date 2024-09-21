package com.example.tienda211.ventas.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

	@InjectMocks
	private ProductoService productoService;

	@InjectMocks
	private PersonaService personaService;

	@InjectMocks
	private VentaService ventaService;

	@Mock
	private VentaRepository ventaRepository;

	@Mock
	private NatsEventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetItems() {
		// Arrange
		Venta item1 = new Venta();
		item1.setId(1L);
		Venta item2 = new Venta();
		item2.setId(2L);

		List<Venta> mockItems = Arrays.asList(item1, item2);
		when(ventaRepository.findAll()).thenReturn(mockItems);

		// Act
		List<Venta> items = ventaService.getItems();

		// Assert
		assertEquals(2, items.size());
		verify(ventaRepository).findAll();
	}

	@Test
	void testGetItemById() {
		// Arrange
		Venta item = new Venta();
		item.setId(1L);

		when(ventaRepository.findById(1L)).thenReturn(Optional.of(item));

		// Act
		Optional<Venta> result = ventaService.getItemById(1L);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(1L, result.get().getId());
		verify(ventaRepository).findById(1L);
	}

	@Test
	void testGetItemById_NotFound() {
		// Arrange
		when(ventaRepository.findById(1L)).thenReturn(Optional.empty());

		// Act
		Optional<Venta> item = ventaService.getItemById(1L);

		// Assert
		assertFalse(item.isPresent());
		verify(ventaRepository).findById(1L);
	}

//	@Test
//	void testCreateItem() {
//		// Arrange
//		Producto producto = new Producto();
//		producto.setId(1L);
//		producto.setNombre("Nuevo");
//		producto.setPrecio(new BigDecimal("10.00"));
//		producto.setCantidad(10);
//		Persona persona = new Persona();
//		persona.setId(1L);
//		persona.setNombre("Ana");
//		VentaDTO itemDTO = new VentaDTO();
//		itemDTO.setProducto_id(producto.getId());
//		itemDTO.setPersona_id(persona.getId());
//		itemDTO.setPrecio(new BigDecimal("15.00"));
//		itemDTO.setCantidad(1);
//
//		// Mockear los servicios dependientes
//		when(productoService.getItemById(1L)).thenReturn(producto);
//		when(personaService.getItemById(1L)).thenReturn(Optional.of(persona));
//
//		Venta item = new Venta();
//		item.setProducto(producto);
//		item.setPersona(persona);
//		item.setPrecio(new BigDecimal("15.00"));
//		item.setCantidad(1);
//		item.setFechaHora(LocalDateTime.now());
//
//		when(ventaRepository.save(any(Venta.class))).thenReturn(item);
//
//		// Act
//		Venta newItem = ventaService.createItem(itemDTO);
//
//		// Assert
//		assertNotNull(newItem);
//		assertEquals(producto, newItem.getProducto());
//		assertEquals(persona, newItem.getPersona());
//
//		// Verificar que los métodos dependientes fueron llamados
//		verify(productoService).getItemById(1L);
//		verify(personaService).getItemById(1L);
//		verify(ventaRepository).save(any(Venta.class));
//
//		// Capturar y verificar el evento publicado
//		@SuppressWarnings("unchecked")
//		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
//		verify(eventPublisher).publishEvent(eq("venta.created"), captor.capture());
//
//		Map<String, Object> payload = captor.getValue();
//		assertEquals(newItem, payload.get("venta"));
//		assertEquals(producto, payload.get("producto"));
//	}

//	@Test
//	void testUpdateItem() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testDeleteItemById() {
		// Arrange

		// Simular la existencia del producto
		when(ventaRepository.existsById(1L)).thenReturn(true);

		// Act
		ventaService.deleteItemById(1L);

		// Assert
		verify(ventaRepository).existsById(1L);
		verify(ventaRepository).deleteById(1L);
	}

}
