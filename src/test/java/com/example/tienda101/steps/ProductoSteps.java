package com.example.tienda101.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.tienda101.productos.application.ProductoService;
import com.example.tienda101.productos.domain.Producto;
import com.example.tienda101.productos.domain.ProductoRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
public class ProductoSteps {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    private Producto producto;
    private Exception exception;

    @Given("un producto con nombre {string} y costo {int} y precio {int} y cantidad {int}")
    public void givenProducto(String nombre, Integer costo, Integer precio, Integer cantidad) {
        producto = new Producto();
        producto.setNombre(nombre);
        producto.setCosto(new BigDecimal(costo));
        producto.setPrecio(new BigDecimal(precio));
        producto.setCantidad(cantidad);
    }

    @When("se crea el producto")
    public void whenSeCreaElProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        productoService.createItem(producto);
    }

    @Then("el producto será guardado exitosamente")
    public void thenProductoGuardadoExitosamente() {
        verify(productoRepository, times(1)).save(producto);
    }

    @Given("un producto existente con id {long} y nombre {string} y costo {int} y precio {int} y cantidad {int}")
    public void givenProductoExistente(Long id, String nombre, Integer costo, Integer precio, Integer cantidad) {
        producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setCosto(new BigDecimal(costo));
        producto.setPrecio(new BigDecimal(precio));
        producto.setCantidad(cantidad);
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
    }

    @When("se actualiza el nombre a {string} y el precio a {int}")
    public void whenSeActualizaProducto(String nuevoNombre, Integer nuevoPrecio) {
        producto.setNombre(nuevoNombre);
        producto.setPrecio(new BigDecimal(nuevoPrecio));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        productoService.updateItem(producto.getId(), producto);
    }

    @Then("el producto tendrá el nombre {string} y precio {int}")
    public void thenProductoActualizado(String nombreEsperado, Integer precioEsperado) {
        assertEquals(nombreEsperado, producto.getNombre());
        assertEquals(new BigDecimal(precioEsperado), producto.getPrecio());
    }

    @Given("no existe un producto con id {long}")
    public void givenProductoNoExiste(Long id) {
        when(productoRepository.existsById(id)).thenReturn(false);
    }

    @When("se intenta eliminar el producto")
    public void whenEliminarProducto() {
        try {
            productoService.deleteItemById(999L);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("ocurrirá un error indicando que el producto no fue encontrado")
    public void thenErrorProductoNoEncontrado() {
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        });
    }
}
