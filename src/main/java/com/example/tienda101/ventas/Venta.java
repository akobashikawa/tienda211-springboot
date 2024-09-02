package com.example.tienda101.ventas;

import com.example.tienda101.productos.Producto;
import com.example.tienda101.personas.Persona;
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
	
	private int cantidad;
	
	@Column(name = "fecha", nullable = false, updatable = false)
	private LocalDate fecha = LocalDate.now();

}
