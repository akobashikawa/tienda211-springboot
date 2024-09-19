package com.example.tienda211.personas.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda211.personas.domain.Persona;

public interface JpaPersonaRepository extends JpaRepository<Persona, Long> {
}
