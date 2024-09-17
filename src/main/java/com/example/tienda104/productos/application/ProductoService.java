package com.example.tienda104.productos.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.tienda104.productos.domain.Producto;
import com.example.tienda104.productos.domain.ProductoRepository;

@Service
public class ProductoService {

	private final ProductoRepository productoRepository;

	public ProductoService(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	public List<Producto> getItems() {
		return productoRepository.findAll();
	}

	public Optional<Producto> getItemById(Long id) {
		return productoRepository.findById(id);
	}

	public Producto createItem(Producto producto) {
		return productoRepository.save(producto);
	}

	public Producto updateItem(Long id, Producto producto) {
		Producto found = productoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado"));
		if (producto.getNombre() != null) {
			found.setNombre(producto.getNombre());
		}
		if (producto.getCosto() != null) {
			found.setCosto(producto.getCosto());
		}
		if (producto.getPrecio() != null) {
			found.setPrecio(producto.getPrecio());
		}
		if (producto.getCantidad() != null) {
			found.setCantidad(producto.getCantidad());
		}
		return productoRepository.save(found);
	}

	public void deleteItemById(Long id) {
		if (!productoRepository.existsById(id)) {
	        throw new RuntimeException("Producto con ID " + id + " no encontrado");
	    }
	    productoRepository.deleteById(id);
	}

	public void decProductoCantidad(Producto producto, int cantidad, int cantidadAnterior) {
        if (producto.getCantidad() < cantidad) {
            throw new RuntimeException("Cantidad insuficiente para el producto " + producto.getId());
        }
        
        int diferencia = cantidad - cantidadAnterior;
        int nuevaCantidad = producto.getCantidad() - diferencia;
        producto.setCantidad(nuevaCantidad);
        updateItem(producto.getId(), producto);
    }
    
    public void decProductoCantidad(Producto producto, int cantidad) {
    	decProductoCantidad(producto, cantidad, 0);
    }
	
}
