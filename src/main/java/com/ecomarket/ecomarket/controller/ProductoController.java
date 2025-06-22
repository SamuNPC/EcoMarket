package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Producto;
import com.ecomarket.ecomarket.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

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

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos registrados.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    })
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{idProducto}")
    public ResponseEntity<Producto> getProductoById(@Parameter(description = "ID del producto") @PathVariable int idProducto) {
        return productoService.getProductoById(idProducto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto creado correctamente")
    })
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente por ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{idProducto}")
    public ResponseEntity<Producto> updateProducto(@Parameter(description = "ID del producto a actualizar") @PathVariable int idProducto, @RequestBody Producto producto) {
        if (productoService.getProductoById(idProducto).isPresent()) {
            producto.setIdProducto(idProducto);
            return ResponseEntity.ok(productoService.saveProducto(producto));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> deleteProducto(@Parameter(description = "ID del producto a eliminar") @PathVariable int idProducto) {
        if (productoService.getProductoById(idProducto).isPresent()) {
            productoService.deleteProducto(idProducto);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
