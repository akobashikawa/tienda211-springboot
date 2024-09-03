package com.example.tienda101.ventas.domain;

import com.example.tienda101.personas.domain.Persona;
import com.example.tienda101.productos.domain.Producto;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "producto_id", nullable = false)
	private Producto producto;

	@ManyToOne
	@JoinColumn(name = "persona_id", nullable = false)
	private Persona persona;

	private BigDecimal precio;
	
	private Integer cantidad;
	
	@Column(name = "fecha", nullable = false, updatable = false)
	private LocalDate fecha = LocalDate.now();

}
