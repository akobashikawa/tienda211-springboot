package com.example.tienda101.productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

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
