package com.example.tienda102.ventas.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.tienda102.personas.application.PersonaService;
import com.example.tienda102.personas.domain.Persona;
import com.example.tienda102.productos.application.ProductoService;
import com.example.tienda102.productos.domain.Producto;
import com.example.tienda102.ventas.application.VentaDTO;
import com.example.tienda102.ventas.application.VentaService;
import com.example.tienda102.ventas.domain.Venta;
import com.example.tienda102.ventas.domain.VentaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class VentaServiceTest {

	@InjectMocks
	private VentaService ventaService;

	@Mock
	private VentaRepository ventaRepository;

	@Mock
	private ProductoService productoService;

	@Mock
	private PersonaService personaService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetItems() {
		Venta venta1 = new Venta();
		Venta venta2 = new Venta();
		List<Venta> ventas = Arrays.asList(venta1, venta2);

		when(ventaRepository.findAll()).thenReturn(ventas);

		List<Venta> result = ventaService.getItems();

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(ventaRepository, times(1)).findAll();
	}

	@Test
	void testGetItemById() {
		Venta venta = new Venta();
		venta.setId(1L);

		when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

		Optional<Venta> result = ventaService.getItemById(1L);

		assertTrue(result.isPresent());
		assertEquals(1L, result.get().getId());
		verify(ventaRepository, times(1)).findById(1L);
	}

	@Test
	void testCreateItem() {
		VentaDTO ventaDTO = new VentaDTO();
		ventaDTO.setProducto_id(1L);
		ventaDTO.setPersona_id(1L);
		ventaDTO.setPrecio(new BigDecimal("100.00"));
		ventaDTO.setCantidad(2);  // Cantidad que se va a vender

		Producto producto = new Producto();
		producto.setId(1L);
		producto.setCantidad(10);  // Stock inicial

		Persona persona = new Persona();
		persona.setId(1L);

		when(productoService.getItemById(1L)).thenReturn(Optional.of(producto));
		when(personaService.getItemById(1L)).thenReturn(Optional.of(persona));
		when(ventaRepository.save(any(Venta.class))).thenReturn(new Venta());

		Venta result = ventaService.createItem(ventaDTO);

		assertNotNull(result);
		verify(productoService, times(1)).getItemById(1L);
		verify(personaService, times(1)).getItemById(1L);
		verify(ventaRepository, times(1)).save(any(Venta.class));

		// Verificar que la cantidad del producto haya disminuido correctamente
		// La cantidad inicial es 10 y se venden 2, así que debería quedar en 8
		assertEquals(8, producto.getCantidad());

		// Verificar que se llama al método updateItem() con la cantidad actualizada
		verify(productoService, times(1)).updateItem(eq(1L), any(Producto.class));
	}

	@Test
	void testCreateItemThrowsExceptionWhenProductNotFound() {
		VentaDTO ventaDTO = new VentaDTO();
		ventaDTO.setProducto_id(1L);

		when(productoService.getItemById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(RuntimeException.class, () -> {
			ventaService.createItem(ventaDTO);
		});

		assertEquals("Producto no encontrado", exception.getMessage());
		verify(productoService, times(1)).getItemById(1L);
		verify(personaService, never()).getItemById(anyLong());
		verify(ventaRepository, never()).save(any(Venta.class));
	}

	@Test
	void testCreateItemThrowsExceptionWhenPersonaNotFound() {
		VentaDTO ventaDTO = new VentaDTO();
		ventaDTO.setProducto_id(1L);
		ventaDTO.setPersona_id(1L);

		Producto producto = new Producto();
		producto.setId(1L);

		when(productoService.getItemById(1L)).thenReturn(Optional.of(producto));
		when(personaService.getItemById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(RuntimeException.class, () -> {
			ventaService.createItem(ventaDTO);
		});

		assertEquals("Persona no encontrada", exception.getMessage());
		verify(productoService, times(1)).getItemById(1L);
		verify(personaService, times(1)).getItemById(1L);
		verify(ventaRepository, never()).save(any(Venta.class));
	}

	@Test
	void testDeleteItemById() {
		ventaService.deleteItemById(1L);
		verify(ventaRepository, times(1)).deleteById(1L);
	}

}
