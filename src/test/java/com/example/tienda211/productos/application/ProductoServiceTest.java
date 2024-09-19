package com.example.tienda211.productos.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tienda211.infrastructure.NatsEventPublisher;
import com.example.tienda211.productos.domain.Producto;
import com.example.tienda211.productos.domain.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

	@Mock
	private ProductoRepository productoRepository;

	@Mock
	private NatsEventPublisher eventPublisher;

	@InjectMocks
	private ProductoService productoService;

	@Test
	void testGetItems() {
		// Datos de prueba
		Producto producto1 = new Producto();
		producto1.setId(1L);
		Producto producto2 = new Producto();
		producto2.setId(2L);

		List<Producto> mockProductos = Arrays.asList(producto1, producto2);
		when(productoRepository.findAll()).thenReturn(mockProductos);

		// Probar el método
		List<Producto> productos = productoService.getItems();

		assertEquals(2, productos.size());
		verify(productoRepository).findAll();
	}

	@Test
	void testGetItemByIdFound() {
		// Dato de prueba
		Producto producto = new Producto();
		producto.setId(1L);

		when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

		// Probar el método
		Optional<Producto> result = productoService.getItemById(1L);

		assertTrue(result.isPresent());
		assertEquals(1L, result.get().getId());
		verify(productoRepository).findById(1L);
	}

	@Test
	void testCreateItem() {
		// Datos de prueba
		Producto producto = new Producto();
		producto.setId(1L);

		when(productoRepository.save(producto)).thenReturn(producto);

		// Probar el método
		Producto newItem = productoService.createItem(producto);

		assertNotNull(newItem);
		assertEquals(1L, newItem.getId());

		// Verificar que se llama al repositorio
		verify(productoRepository).save(producto);

		// Verificar que se llama al publicador de eventos
//		Map<String, Object> expectedPayload = new HashMap<>();
//		expectedPayload.put("producto", newItem);
//		verify(eventPublisher).publishEvent("producto.created", expectedPayload);
		
		// Capturar y verificar el evento publicado
	    ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
	    verify(eventPublisher).publishEvent(eq("producto.created"), captor.capture());
	    assertEquals(newItem, captor.getValue().get("producto"));
	}

	@Test
	void testUpdateItem() {
		// Dato de prueba
		Producto producto = new Producto();
		producto.setId(1L);
		producto.setNombre("Producto Test");

		Producto updateData = new Producto();
		updateData.setNombre("Producto Actualizado");

		when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
		when(productoRepository.save(producto)).thenReturn(producto);

		// Probar el método
		Producto updatedItem = productoService.updateItem(1L, updateData);

		assertEquals("Producto Actualizado", updatedItem.getNombre());

		// Verificar que se llama al repositorio
		verify(productoRepository).findById(1L);
		verify(productoRepository).save(producto);

		// Verificar que se llama al publicador de eventos
//		Map<String, Object> expectedPayload = new HashMap<>();
//		expectedPayload.put("producto", updatedItem);
//		verify(eventPublisher).publishEvent("producto.updated", expectedPayload);
		
		// Capturar y verificar el evento publicado
	    ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
	    verify(eventPublisher).publishEvent(eq("producto.updated"), captor.capture());
	    
	    // Asegurarse de que el mapa capturado contiene el producto actualizado
	    assertEquals(updatedItem, captor.getValue().get("producto"));
	}

	@Test
	void testDeleteItemById() {
		// Simular la existencia del producto
		when(productoRepository.existsById(1L)).thenReturn(true);

		// Probar el método
		productoService.deleteItemById(1L);

		// Verificar que se llama al repositorio para eliminar
		verify(productoRepository).existsById(1L);
		verify(productoRepository).deleteById(1L);
	}

	@Test
	void testDecProductoCantidad() {
		// Dato de prueba
		Producto producto = new Producto();
		producto.setId(1L);
		producto.setCantidad(10);

		// Simular el comportamiento de productoRepository.findById()
		when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

		// Probar el método
		productoService.decProductoCantidad(producto, 5);

		assertEquals(5, producto.getCantidad());

		// Verificar que se llama al método updateItem
		verify(productoRepository).save(producto);
	}
}
