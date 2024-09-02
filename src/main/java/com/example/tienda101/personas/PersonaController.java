package com.example.tienda101.personas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

	@Autowired
	private PersonaService personaService;

	@GetMapping
	public List<Persona> getItems() {
		return personaService.findAll();
	}

	@GetMapping("/{id}")
	public Persona getItemById(@PathVariable Long id) {
		return personaService.findById(id);
	}

	@PostMapping
	public Persona createItem(@RequestBody Persona persona) {
		return personaService.save(persona);
	}

	@PutMapping("/{id}")
	public Persona updateItem(@PathVariable Long id, @RequestBody Persona persona) {
		persona.setId(id);
		return personaService.save(persona);
	}

	@DeleteMapping("/{id}")
	public void deleteItemById(@PathVariable Long id) {
		personaService.deleteById(id);
	}
}
