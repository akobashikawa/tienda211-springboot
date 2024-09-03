package com.example.tienda101.ventas.application;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class VentaDTO {
	private Long id;
    private Long persona_id;
    private Long producto_id;
    private BigDecimal precio;
    private int cantidad;

}
