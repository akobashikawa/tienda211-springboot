package com.example.tienda101.personas.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.tienda101.personas.application.PersonaService;
import com.example.tienda101.personas.domain.Persona;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

	@Autowired
	private PersonaService personaService;

	@GetMapping
	public List<Persona> getItems() {
		return personaService.getItems();
	}

	@GetMapping("/{id}")
	public Persona getItemById(@PathVariable Long id) {
		return personaService.getItemById(id);
	}

	@PostMapping
	public Persona createItem(@RequestBody Persona persona) {
		return personaService.createItem(persona);
	}

	@PutMapping("/{id}")
	public Persona updateItem(@PathVariable Long id, @RequestBody Persona persona) {
		return personaService.updateItem(id, persona);
	}

	@DeleteMapping("/{id}")
	public void deleteItemById(@PathVariable Long id) {
		personaService.deleteItemById(id);
	}
}
