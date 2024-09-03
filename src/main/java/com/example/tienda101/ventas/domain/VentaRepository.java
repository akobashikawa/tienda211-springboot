package com.example.tienda101.ventas.domain;

import java.util.List;
import java.util.Optional;

public interface VentaRepository {
	List<Venta> findAll();

	Optional<Venta> findById(Long id);

	Venta save(Venta producto);

	void deleteById(Long id);
}
