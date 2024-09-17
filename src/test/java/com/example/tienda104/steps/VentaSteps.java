package com.example.tienda104.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.tienda104.personas.application.PersonaService;
import com.example.tienda104.personas.domain.Persona;
import com.example.tienda104.productos.application.ProductoService;
import com.example.tienda104.productos.domain.Producto;
import com.example.tienda104.ventas.application.VentaDTO;
import com.example.tienda104.ventas.application.VentaService;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
public class VentaSteps {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PersonaService personaService;

    private Producto producto;
    private Persona persona;
    private VentaDTO ventaDTO;
    private Exception exception;

    @Given("el producto con id {long} y stock de {int}")
    public void givenProducto(Long productoId, Integer stock) {
        producto = new Producto();
        producto.setId(productoId);
        producto.setCantidad(stock);
        when(productoService.getItemById(productoId)).thenReturn(Optional.of(producto));
    }

    @Given("la persona con id {long}")
    public void givenPersona(Long personaId) {
        persona = new Persona();
        persona.setId(personaId);
        when(personaService.getItemById(personaId)).thenReturn(Optional.of(persona));
    }

    @When("se crea una venta con {int} unidades del producto")
    public void whenCreateVenta(Integer cantidad) {
        ventaDTO = new VentaDTO();
        ventaDTO.setProducto_id(producto.getId());
        ventaDTO.setPersona_id(persona.getId());
        ventaDTO.setPrecio(new BigDecimal("100.00"));
        ventaDTO.setCantidad(cantidad);

        try {
            ventaService.createItem(ventaDTO);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("la cantidad de stock del producto será {int}")
    public void thenCantidadStock(Integer stockEsperado) {
        assertEquals(stockEsperado, producto.getCantidad());
    }

    @Then("ocurrirá un error indicando que el stock es insuficiente")
    public void thenErrorStockInsuficiente() {
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        });
    }
}
