package com.example.tienda211.productos.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda211.productos.domain.Producto;

public interface JpaProductoRepository extends JpaRepository<Producto, Long> {
}
