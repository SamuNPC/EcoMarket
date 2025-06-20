package com.ecomarket.ecomarket.service;

import com.ecomarket.ecomarket.model.Producto;
import com.ecomarket.ecomarket.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(int idProducto) {
        return productoRepository.findById(idProducto);
    }

    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void deleteProducto(int idProducto) {
        productoRepository.deleteById(idProducto);
    }

    public List<Producto> findByNombre(String nombreProducto) {
        return productoRepository.findByNombreProducto(nombreProducto);
    }

    public List<Producto> findByStock(int stock) {
        return productoRepository.findByStock(stock);
    }
}