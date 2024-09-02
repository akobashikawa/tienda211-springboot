package com.example.tienda101.productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Producto getItemById(@PathVariable Long id) {
        return productoService.getItemById(id);
    }

    @PostMapping
    public Producto createItem(@RequestBody Producto producto) {
        return productoService.createItem(producto);
    }

    @PutMapping("/{id}")
    public Producto updateItem(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.updateItem(id, producto);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable Long id) {
        productoService.deleteItemById(id);
    }
}
