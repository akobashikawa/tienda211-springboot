package com.example.tienda103.productos.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda103.productos.domain.Producto;

public interface JpaProductoRepository extends JpaRepository<Producto, Long> {
}
