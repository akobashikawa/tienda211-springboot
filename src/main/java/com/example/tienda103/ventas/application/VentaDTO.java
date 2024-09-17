package com.example.tienda103.ventas.application;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class VentaDTO {
	private Long id;
    private Long persona_id;
    private Long producto_id;
    private BigDecimal precio;
    private Integer cantidad;
    
    public VentaDTO() {
    }
    
    public VentaDTO(Long persona_id, Long producto_id, BigDecimal precio, Integer cantidad) {
        this.persona_id = persona_id;
        this.producto_id = producto_id;
        this.precio = precio;
        this.cantidad = cantidad;
    }


}
