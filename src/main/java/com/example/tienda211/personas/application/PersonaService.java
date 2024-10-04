package com.example.tienda211.personas.application;

import org.springframework.stereotype.Service;

import com.example.tienda211.infrastructure.NatsEventPublisher;
import com.example.tienda211.personas.domain.Persona;
import com.example.tienda211.personas.domain.PersonaRepository;
import com.example.tienda211.productos.domain.Producto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;
    
    private final NatsEventPublisher eventPublisher;

    public PersonaService(PersonaRepository personaRepository, NatsEventPublisher eventPublisher) {
        this.personaRepository = personaRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Persona> getItems() {
        return personaRepository.findAll();
    }

    public Persona getItemById(Long id) {
        return personaRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Persona no encontrada: " + id));
    }

    public Persona createItem(Persona persona) {
        Persona newItem = personaRepository.save(persona);
        Map<String, Object> payload = new HashMap<>();
		payload.put("personaId", String.valueOf(newItem.getId()));
		eventPublisher.publishEvent("persona.created", payload);
        return newItem;
    }
    
    public Persona updateItem(Long id, Persona persona) {
    	Persona found = personaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Persona no encontrada: " + id));
    	found.setNombre(persona.getNombre());
        Persona updatedItem = personaRepository.save(found);
        
        Map<String, Object> payload = new HashMap<>();
		payload.put("personaId", String.valueOf(updatedItem.getId()));
		eventPublisher.publishEvent("persona.updated", payload);
		
        return updatedItem;
    }

    public void deleteItemById(Long id) {
    	if (!personaRepository.existsById(id)) {
	        throw new RuntimeException("Persona con ID " + id + " no encontrada");
	    }
        personaRepository.deleteById(id);
    }
}
