package com.example.tienda101.personas.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda101.personas.domain.Persona;

public interface JpaPersonaRepository extends JpaRepository<Persona, Long> {
}
