package com.example.tienda211.productos.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tienda211.productos.application.ProductoService;
import com.example.tienda211.productos.domain.Producto;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    public ResponseEntity<List<Producto>> getItems() {
        List<Producto> productos = productoService.getItems();
        return ResponseEntity.ok(productos); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getItemById(@PathVariable Long id) {
        try {
        	Producto found = productoService.getItemById(id);
        	return ResponseEntity.ok(found); // 200 OK
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } 
    }

    @PostMapping
    public ResponseEntity<Producto> createItem(@RequestBody Producto producto) {
        try {
            Producto createdItem = productoService.createItem(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateItem(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto updatedItem = productoService.updateItem(id, producto);
            return ResponseEntity.ok(updatedItem); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(@PathVariable Long id) {
        try {
            productoService.deleteItemById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
