package com.example.tienda211.ventas.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tienda211.infrastructure.SocketIOService;
import com.example.tienda211.productos.domain.Producto;
import com.example.tienda211.ventas.application.VentaDTO;
import com.example.tienda211.ventas.application.VentaService;
import com.example.tienda211.ventas.domain.Venta;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;
    
    @GetMapping
    public ResponseEntity<List<Venta>> getItems() {
        List<Venta> ventas = ventaService.getItems();
        return ResponseEntity.ok(ventas); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getItemById(@PathVariable Long id) {
    	try {
    		Venta found = ventaService.getItemById(id);
        	return ResponseEntity.ok(found); // 200 OK
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } 
    }

    @PostMapping
    public ResponseEntity<Venta> createItem(@RequestBody VentaDTO ventaDTO) {
        try {
            Venta createdItem = ventaService.createItem(ventaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateItem(@PathVariable Long id, @RequestBody VentaDTO ventaDTO) {
        try {
            Venta updatedItem = ventaService.updateItem(id, ventaDTO);
            return ResponseEntity.ok(updatedItem); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(@PathVariable Long id) {
        try {
            ventaService.deleteItemById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
    
}
