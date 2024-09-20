package com.example.tienda211.productos.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
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
import com.example.tienda211.productos.domain.Producto;
import com.example.tienda211.productos.domain.ProductoRepository;

class ProductoServiceTest {

	@InjectMocks
	private ProductoService productoService; // El servicio que queremos probar

	@Mock
	private ProductoRepository productoRepository; // Mock de la dependencia

	@Mock
	private NatsEventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Inicializa los mocks
	}

	@Test
	void testGetItems() {
		// Arrange
		Producto item1 = new Producto();
		item1.setId(1L);
		Producto item2 = new Producto();
		item2.setId(2L);

		List<Producto> mockItems = Arrays.asList(item1, item2);
		when(productoRepository.findAll()).thenReturn(mockItems);

		// Act
		List<Producto> items = productoService.getItems();

		// Assert
		assertEquals(2, items.size());
		verify(productoRepository).findAll();
	}

	@Test
	void testGetItemById() {
		// Arrange
		Producto item = new Producto();
		item.setId(1L);

		when(productoRepository.findById(1L)).thenReturn(Optional.of(item));

		// Act
		Optional<Producto> result = productoService.getItemById(1L);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(1L, result.get().getId());
		verify(productoRepository).findById(1L);
	}
	
	@Test
	void testGetItemById_NotFound() {
		// Arrange
	    when(productoRepository.findById(1L)).thenReturn(Optional.empty());

	    // Act
	    Optional<Producto> item = productoService.getItemById(1L);

	    // Assert
	    assertFalse(item.isPresent()); // Debe ser false porque no hay producto
	    verify(productoRepository).findById(1L); // Verificar que se llamó al repositorio
	}

	@Test
	void testCreateItem() {
		// Arrange
		Producto item = new Producto();
		item.setId(1L);

		when(productoRepository.save(item)).thenReturn(item);

		// Act
		Producto newItem = productoService.createItem(item);

		// Assert
		assertNotNull(newItem);
		assertEquals(1L, newItem.getId());

		verify(productoRepository).save(item);

		// Verificar que se llama al publicador de eventos
//		Map<String, Object> expectedPayload = new HashMap<>();
//		expectedPayload.put("producto", newItem);
//		verify(eventPublisher).publishEvent("producto.created", expectedPayload);

		// Capturar y verificar el evento publicado
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(eventPublisher).publishEvent(eq("producto.created"), captor.capture());
		assertEquals(newItem, captor.getValue().get("producto"));
	}

	@Test
	void testUpdateItem() {
		// Arrange
		Producto item = new Producto();
		item.setId(1L);
		item.setNombre("Nuevo");

		Producto dataItem = new Producto();
		dataItem.setNombre("Actualizado");

		when(productoRepository.findById(1L)).thenReturn(Optional.of(item));
		when(productoRepository.save(item)).thenReturn(item);

		// Act
		Producto updatedItem = productoService.updateItem(1L, dataItem);

		assertEquals("Actualizado", updatedItem.getNombre());

		// Verificar que se llama al repositorio
		verify(productoRepository).findById(1L);
		verify(productoRepository).save(item);

		// Verificar que se llama al publicador de eventos
//				Map<String, Object> expectedPayload = new HashMap<>();
//				expectedPayload.put("producto", updatedItem);
//				verify(eventPublisher).publishEvent("producto.updated", expectedPayload);

		// Capturar y verificar el evento publicado
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(eventPublisher).publishEvent(eq("producto.updated"), captor.capture());

		// Asegurarse de que el mapa capturado contiene el producto actualizado
		assertEquals(updatedItem, captor.getValue().get("producto"));
	}

	@Test
	void testDeleteItemById() {
		// Arrange
		
		// Simular la existencia del producto
		when(productoRepository.existsById(1L)).thenReturn(true);

		// Act
		productoService.deleteItemById(1L);

		// Assert
		verify(productoRepository).existsById(1L);
		verify(productoRepository).deleteById(1L);
	}

}
