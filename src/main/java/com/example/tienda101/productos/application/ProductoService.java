package com.example.tienda101.productos.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tienda101.productos.domain.Producto;
import com.example.tienda101.productos.domain.ProductoRepository;

@Service
public class ProductoService {

	private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    
    public List<Producto> getItems() {
        return productoRepository.findAll();
    }

    public Producto getItemById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto createItem(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public Producto updateItem(Long id, Producto producto) {
    	producto.setId(id);
        return productoRepository.save(producto);
    }

    public void deleteItemById(Long id) {
        productoRepository.deleteById(id);
    }
}
