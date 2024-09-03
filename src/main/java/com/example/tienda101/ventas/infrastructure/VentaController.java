package com.example.tienda101.ventas.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.tienda101.ventas.application.VentaDTO;
import com.example.tienda101.ventas.application.VentaService;
import com.example.tienda101.ventas.domain.Venta;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> getItems() {
        return ventaService.getItems();
    }

    @GetMapping("/{id}")
    public Venta getItemById(@PathVariable Long id) {
        return ventaService.getItemById(id);
    }

    @PostMapping
    public Venta createItem(@RequestBody VentaDTO ventaDTO) {
        return ventaService.createItem(ventaDTO);
    }

    @PutMapping("/{id}")
    public Venta updateItem(@PathVariable Long id, @RequestBody VentaDTO ventaDTO) {
        return ventaService.updateItem(id, ventaDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable Long id) {
        ventaService.deleteItemById(id);
    }
}
