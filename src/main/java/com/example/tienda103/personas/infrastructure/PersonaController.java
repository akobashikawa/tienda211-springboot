package com.example.tienda103.personas.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tienda103.personas.application.PersonaService;
import com.example.tienda103.personas.domain.Persona;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> getItems() {
        List<Persona> personas = personaService.getItems();
        return ResponseEntity.ok(personas); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getItemById(@PathVariable Long id) {
        Optional<Persona> optionalPersona = personaService.getItemById(id);
        return optionalPersona
                .map(persona -> ResponseEntity.ok(persona)) // 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found
    }

    @PostMapping
    public ResponseEntity<Persona> createItem(@RequestBody Persona persona) {
        try {
            Persona createdPersona = personaService.createItem(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPersona); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> updateItem(@PathVariable Long id, @RequestBody Persona persona) {
        try {
            Persona updatedPersona = personaService.updateItem(id, persona);
            return ResponseEntity.ok(updatedPersona); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(@PathVariable Long id) {
        try {
            personaService.deleteItemById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
