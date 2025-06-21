package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Producto;
import com.ecomarket.ecomarket.service.ProductoService;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<Producto> getProductoById(@PathVariable int idProducto) {
        return productoService.getProductoById(idProducto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<Producto> updateProducto(@PathVariable int idProducto, @RequestBody Producto producto) {
        if (productoService.getProductoById(idProducto).isPresent()) {
            producto.setIdProducto(idProducto);
            return ResponseEntity.ok(productoService.saveProducto(producto));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> deleteProducto(@PathVariable int idProducto) {
        if (productoService.getProductoById(idProducto).isPresent()) {
            productoService.deleteProducto(idProducto);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
