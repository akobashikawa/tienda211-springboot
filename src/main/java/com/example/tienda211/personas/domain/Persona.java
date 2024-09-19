package com.example.tienda211.personas.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "personas")
@Data
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	
	public Persona() {
	}
	
	public Persona(String nombre) {
		this.nombre = nombre;
	}
	
	public Persona(Long id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

}
