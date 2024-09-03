package com.example.tienda101.productos.application;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductoUpdateDTO {
	private String nombre;
	private BigDecimal costo;
	private BigDecimal precio;
	private Integer cantidad;
}
