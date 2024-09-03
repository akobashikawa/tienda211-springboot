package com.example.tienda101.ventas.infrastructure;

import com.example.tienda101.ventas.domain.Venta;
import com.example.tienda101.ventas.domain.VentaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VentaJpaRepository implements VentaRepository {

    private final JpaVentaRepository jpaVentaRepository;

    public VentaJpaRepository(JpaVentaRepository jpaVentaRepository) {
        this.jpaVentaRepository = jpaVentaRepository;
    }

    @Override
    public List<Venta> findAll() {
        return jpaVentaRepository.findAll();
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return jpaVentaRepository.findById(id);
    }

    @Override
    public Venta save(Venta producto) {
        return jpaVentaRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        jpaVentaRepository.deleteById(id);
    }
}
