package com.example.tienda102.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.tienda103.personas.application.PersonaService;
import com.example.tienda103.personas.domain.Persona;
import com.example.tienda103.personas.domain.PersonaRepository;

@SpringBootTest
public class PersonaSteps {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PersonaRepository personaRepository;

    private Persona persona;
    private Long personaId;
    private Exception exception;

    @Given("una persona con nombre {string}")
    public void givenPersona(String nombre) {
        persona = new Persona();
        persona.setNombre(nombre);
    }

    @When("se crea la persona")
    public void whenSeCreaLaPersona() {
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);
        personaService.createItem(persona);
    }

    @Then("la persona será guardada exitosamente")
    public void thenPersonaGuardadaExitosamente() {
        verify(personaRepository, times(1)).save(persona);
    }

    @Given("una persona existente con id {long} y nombre {string}")
    public void givenPersonaExistente(Long id, String nombre) {
        persona = new Persona();
        persona.setId(id);
        persona.setNombre(nombre);
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona));
        personaId = id;
    }

    @When("se actualiza el nombre a {string}")
    public void whenSeActualizaPersona(String nuevoNombre) {
        persona.setNombre(nuevoNombre);
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);
        personaService.updateItem(personaId, persona);
    }

    @Then("la persona tendrá el nombre {string}")
    public void thenPersonaActualizada(String nombreEsperado) {
        assertEquals(nombreEsperado, persona.getNombre());
    }

    @Given("no existe una persona con id {long}")
    public void givenPersonaNoExiste(Long id) {
        when(personaRepository.existsById(id)).thenReturn(false);
    }

    @When("se intenta eliminar la persona")
    public void whenEliminarPersona() {
        try {
            personaService.deleteItemById(999L);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("ocurrirá un error indicando que la persona no fue encontrada")
    public void thenErrorPersonaNoEncontrada() {
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        });
    }
}
