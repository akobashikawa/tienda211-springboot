package com.example.tienda211.ventas.domain;

import java.util.List;
import java.util.Optional;

public interface VentaRepository {
	List<Venta> findAll();

	Optional<Venta> findById(Long id);

	Venta save(Venta venta);

	void deleteById(Long id);

	boolean existsById(Long id);
}
