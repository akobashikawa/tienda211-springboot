package com.example.tienda104.ventas.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tienda104.infrastructure.SocketIOService;
import com.example.tienda104.ventas.application.VentaDTO;
import com.example.tienda104.ventas.application.VentaService;
import com.example.tienda104.ventas.domain.Venta;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private SocketIOService socketIOService;

    @GetMapping
    public ResponseEntity<List<Venta>> getItems() {
        List<Venta> ventas = ventaService.getItems();
        return ResponseEntity.ok(ventas); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getItemById(@PathVariable Long id) {
        return ventaService.getItemById(id)
                .map(venta -> ResponseEntity.ok(venta)) // 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found
    }

    @PostMapping
    public ResponseEntity<Venta> createItem(@RequestBody VentaDTO ventaDTO) {
        try {
            Venta createdItem = ventaService.createItem(ventaDTO);
            socketIOService.emitItem("ventaCreated", createdItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateItem(@PathVariable Long id, @RequestBody VentaDTO ventaDTO) {
        try {
            Venta updatedItem = ventaService.updateItem(id, ventaDTO);
            socketIOService.emitItem("ventaUpdated", updatedItem);
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
