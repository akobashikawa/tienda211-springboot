package com.example.tienda101.productos.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda101.productos.domain.Producto;

public interface JpaProductoRepository extends JpaRepository<Producto, Long> {
}
