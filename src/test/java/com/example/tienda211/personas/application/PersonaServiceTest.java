package com.example.tienda211.personas.application;

import com.example.tienda211.infrastructure.NatsEventPublisher;
import com.example.tienda211.personas.domain.Persona;
import com.example.tienda211.personas.domain.PersonaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private NatsEventPublisher eventPublisher;

    @InjectMocks
    private PersonaService personaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetItems() {
        // Arrange
        Persona persona1 = new Persona(1L, "John Doe");
        Persona persona2 = new Persona(2L, "Jane Doe");
        when(personaRepository.findAll()).thenReturn(Arrays.asList(persona1, persona2));

        // Act
        var result = personaService.getItems();

        // Assert
        assertEquals(2, result.size());
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void testGetItemById() {
        // Arrange
        Persona persona = new Persona(1L, "John Doe");
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        // Act
        var result = personaService.getItemById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getNombre());
        verify(personaRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateItem() {
        // Arrange
        Persona persona = new Persona(1L, "John Doe");
        when(personaRepository.save(persona)).thenReturn(persona);

        // Act
        Persona result = personaService.createItem(persona);

        // Assert
        assertEquals(persona, result);
        verify(personaRepository, times(1)).save(persona);
        
        // Verificar que el evento fue publicado
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(eventPublisher, times(1)).publishEvent(eq("persona.created"), captor.capture());
        assertEquals(persona, captor.getValue().get("persona"));
    }

    @Test
    void testUpdateItem() {
        // Arrange
        Persona persona = new Persona(1L, "John Doe");
        when(personaRepository.save(persona)).thenReturn(persona);

        // Act
        Persona result = personaService.updateItem(1L, persona);

        // Assert
        assertEquals(persona, result);
        verify(personaRepository, times(1)).save(persona);

        // Verificar que el evento fue publicado
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(eventPublisher, times(1)).publishEvent(eq("persona.updated"), captor.capture());
        assertEquals(persona, captor.getValue().get("persona"));
    }

    @Test
    void testDeleteItemById() {
        // Arrange
        long id = 1L;
        
        // Act
        personaService.deleteItemById(id);

        // Assert
        verify(personaRepository, times(1)).deleteById(id);
    }
}
