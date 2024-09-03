package com.example.tienda101.productos.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.tienda101.productos.application.ProductoService;
import com.example.tienda101.productos.application.ProductoUpdateDTO;
import com.example.tienda101.productos.domain.Producto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getItems() {
        return productoService.getItems();
    }

    @GetMapping("/{id}")
    public Optional<Producto> getItemById(@PathVariable Long id) {
        return productoService.getItemById(id);
    }

    @PostMapping
    public Producto createItem(@RequestBody Producto producto) {
        return productoService.createItem(producto);
    }

    @PutMapping("/{id}")
    public Producto updateItem(@PathVariable Long id, @RequestBody ProductoUpdateDTO productoDTO) {
    	Producto producto = new Producto();
    	if (productoDTO.getNombre() != null) {
            producto.setNombre(productoDTO.getNombre());
        }
    	if (productoDTO.getCosto() != null) {
    		producto.setCosto(productoDTO.getCosto());
    	}
        if (productoDTO.getPrecio() != null) {
            producto.setPrecio(productoDTO.getPrecio());
        }
        if (productoDTO.getCantidad() != null) {
            producto.setCantidad(productoDTO.getCantidad());
        }
        return productoService.updateItem(id, producto);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable Long id) {
        productoService.deleteItemById(id);
    }
}
