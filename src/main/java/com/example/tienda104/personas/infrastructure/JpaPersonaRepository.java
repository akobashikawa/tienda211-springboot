package com.example.tienda104.personas.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda104.personas.domain.Persona;

public interface JpaPersonaRepository extends JpaRepository<Persona, Long> {
}
