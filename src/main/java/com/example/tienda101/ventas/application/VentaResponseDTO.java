package com.example.tienda101.ventas.application;

import com.example.tienda101.productos.domain.Producto;
import com.example.tienda101.personas.domain.Persona;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VentaResponseDTO {
	private Long id;
	private Long producto_id;
	private Producto producto;
	private Long persona_id;
	private Persona persona;
	private BigDecimal precio;
	private int cantidad;
	private LocalDate fecha;
}
