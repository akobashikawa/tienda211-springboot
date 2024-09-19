package com.example.tienda211.ventas.infrastructure;

import org.springframework.stereotype.Repository;

import com.example.tienda211.ventas.domain.Venta;
import com.example.tienda211.ventas.domain.VentaRepository;

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
    public Venta save(Venta venta) {
        return jpaVentaRepository.save(venta);
    }

    @Override
    public void deleteById(Long id) {
        jpaVentaRepository.deleteById(id);
    }
}
