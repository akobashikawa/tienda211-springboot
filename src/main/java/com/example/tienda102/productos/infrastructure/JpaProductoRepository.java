package com.example.tienda102.productos.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda102.productos.domain.Producto;

public interface JpaProductoRepository extends JpaRepository<Producto, Long> {
}
