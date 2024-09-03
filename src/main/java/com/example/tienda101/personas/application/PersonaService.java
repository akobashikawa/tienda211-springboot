package com.example.tienda101.personas.application;

import org.springframework.stereotype.Service;

import com.example.tienda101.personas.domain.Persona;
import com.example.tienda101.personas.domain.PersonaRepository;

import java.util.List;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<Persona> getItems() {
        return personaRepository.findAll();
    }

    public Persona getItemById(Long id) {
        return personaRepository.findById(id).orElse(null);
    }

    public Persona createItem(Persona persona) {
        return personaRepository.save(persona);
    }
    
    public Persona updateItem(Long id, Persona persona) {
    	persona.setId(id);
        return personaRepository.save(persona);
    }

    public void deleteItemById(Long id) {
        personaRepository.deleteById(id);
    }
}
