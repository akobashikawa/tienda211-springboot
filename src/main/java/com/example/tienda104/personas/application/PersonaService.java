package com.example.tienda104.personas.application;

import org.springframework.stereotype.Service;

import com.example.tienda104.infrastructure.NatsEventPublisher;
import com.example.tienda104.personas.domain.Persona;
import com.example.tienda104.personas.domain.PersonaRepository;

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

    public Optional<Persona> getItemById(Long id) {
        return personaRepository.findById(id);
    }

    public Persona createItem(Persona persona) {
        Persona newItem = personaRepository.save(persona);
        Map<String, Object> payload = new HashMap<>();
		payload.put("persona", newItem);
		eventPublisher.publishEvent("persona.created", payload);
        return newItem;
    }
    
    public Persona updateItem(Long id, Persona persona) {
    	persona.setId(id);
        Persona updatedItem = personaRepository.save(persona);
        Map<String, Object> payload = new HashMap<>();
		payload.put("persona", updatedItem);
		eventPublisher.publishEvent("persona.updated", payload);
        return updatedItem;
    }

    public void deleteItemById(Long id) {
        personaRepository.deleteById(id);
    }
}
