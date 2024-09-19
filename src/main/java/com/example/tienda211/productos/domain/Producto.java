package com.example.tienda211.productos.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private BigDecimal costo;
	private BigDecimal precio;
	private Integer cantidad;
	
	public Producto() {
		//...
	}
	
	public Producto(Long id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

}
