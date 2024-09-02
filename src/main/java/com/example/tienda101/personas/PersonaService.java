package com.example.tienda101.personas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

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
