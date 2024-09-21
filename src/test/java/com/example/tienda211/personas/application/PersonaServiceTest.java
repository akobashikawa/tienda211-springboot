package com.example.tienda211.personas.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
import com.example.tienda211.personas.domain.Persona;
import com.example.tienda211.personas.domain.PersonaRepository;
import com.example.tienda211.productos.domain.Producto;

class PersonaServiceTest {

	@InjectMocks
	private PersonaService personaService;

	@Mock
	private PersonaRepository personaRepository;

	@Mock
	private NatsEventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetItems() {
		// Arrange
		Persona item1 = new Persona();
		item1.setId(1L);
		Persona item2 = new Persona();
		item2.setId(2L);

		List<Persona> mockItems = Arrays.asList(item1, item2);
		when(personaRepository.findAll()).thenReturn(mockItems);

		// Act
		List<Persona> items = personaService.getItems();

		// Assert
		assertEquals(2, items.size());
		verify(personaRepository).findAll();
	}

	@Test
	void testGetItemById() {
		// Arrange
		Persona item = new Persona();
		item.setId(1L);

		when(personaRepository.findById(1L)).thenReturn(Optional.of(item));

		// Act
		Persona found = personaService.getItemById(1L);

		// Assert
		assertNotNull(found);
		assertEquals(1L, found.getId());
		verify(personaRepository).findById(1L);
	}

	@Test
	void testGetItemById_NotFound() {
		// Arrange
		when(personaRepository.findById(1L)).thenReturn(Optional.empty());

		// Act
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			personaService.getItemById(1L);
	    });

		// Assert
		assertEquals("Persona no encontrada: 1", exception.getMessage());
		verify(personaRepository).findById(1L);
	}

	@Test
	void testCreateItem() {
		// Arrange
		Persona item = new Persona();
		item.setId(1L);

		when(personaRepository.save(item)).thenReturn(item);

		// Act
		Persona newItem = personaService.createItem(item);

		// Assert
		assertNotNull(newItem);
		assertEquals(1L, newItem.getId());

		verify(personaRepository).save(item);

		// Verificar que se llama al publicador de eventos
//				Map<String, Object> expectedPayload = new HashMap<>();
//				expectedPayload.put("persona", newItem);
//				verify(eventPublisher).publishEvent("persona.created", expectedPayload);

		// Capturar y verificar el evento publicado
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(eventPublisher).publishEvent(eq("persona.created"), captor.capture());
		assertEquals(newItem, captor.getValue().get("persona"));
	}

	@Test
	void testUpdateItem() {
		// Arrange
		Persona item = new Persona();
		item.setId(1L);
		item.setNombre("Ana");

		Persona dataItem = new Persona();
		dataItem.setNombre("Betty");

		when(personaRepository.findById(1L)).thenReturn(Optional.of(item));
		when(personaRepository.save(any(Persona.class))).thenReturn(item);

		// Act
		Persona updatedItem = personaService.updateItem(1L, dataItem);

		// Assert
		assertEquals("Betty", updatedItem.getNombre());

		// Verificar que se llama al repositorio
		verify(personaRepository).findById(1L);
		verify(personaRepository).save(item);

//		// Verificar que se llama al publicador de eventos
////						Map<String, Object> expectedPayload = new HashMap<>();
////						expectedPayload.put("persona", updatedItem);
////						verify(eventPublisher).publishEvent("persona.updated", expectedPayload);

		// Capturar y verificar el evento publicado
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
		verify(eventPublisher).publishEvent(eq("persona.updated"), captor.capture());

		// Asegurarse de que el mapa capturado contiene el producto actualizado
		assertEquals(updatedItem, captor.getValue().get("persona"));
	}

	@Test
	void testDeleteItemById() {
		// Arrange

		// Simular la existencia del producto
		when(personaRepository.existsById(1L)).thenReturn(true);

		// Act
		personaService.deleteItemById(1L);

		// Assert
		verify(personaRepository).existsById(1L);
		verify(personaRepository).deleteById(1L);
	}

}
