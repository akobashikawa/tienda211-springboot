package com.example.tienda102.personas.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda102.personas.domain.Persona;

public interface JpaPersonaRepository extends JpaRepository<Persona, Long> {
}
