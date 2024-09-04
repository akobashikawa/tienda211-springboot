package com.example.tienda101.ventas.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.tienda101.personas.domain.Persona;
import com.example.tienda101.productos.domain.Producto;
import com.example.tienda101.ventas.application.VentaDTO;
import com.example.tienda101.ventas.application.VentaResponseDTO;
import com.example.tienda101.ventas.application.VentaService;
import com.example.tienda101.ventas.domain.Venta;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<VentaResponseDTO> getItems() {
        return ventaService.getItems().stream().map(this::mapToVentaResponseDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VentaResponseDTO getItemById(@PathVariable Long id) {
        return mapToVentaResponseDTO(ventaService.getItemById(id));
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
    
    private VentaResponseDTO mapToVentaResponseDTO(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(venta.getId());
        dto.setProducto(venta.getProducto());
        dto.setProducto_id(venta.getProducto().getId());
        dto.setPersona(venta.getPersona());
        dto.setPersona_id(venta.getPersona().getId());
        dto.setPrecio(venta.getPrecio());
        dto.setCantidad(venta.getCantidad());
        dto.setFecha(venta.getFecha());
        return dto;
    }
}
