package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Producto;
import com.ecomarket.ecomarket.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;

    // Constants for link relations
    private static final String REL_PRODUCTOS = "productos";

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    @GetMapping
    public CollectionModel<EntityModel<Producto>> getAllProductos() {
        List<EntityModel<Producto>> productos = productoService.getAllProductos().stream()
                .map(this::addLinksToProducto)
                .toList();

        return CollectionModel.of(productos)
                .add(linkTo(ProductoController.class).withSelfRel());
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto según su ID.")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{idProducto}")
    public ResponseEntity<EntityModel<Producto>> getProductoById(
            @Parameter(description = "ID del producto") @PathVariable int idProducto) {
        return productoService.getProductoById(idProducto)
                .map(this::addLinksToProducto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto.")
    @ApiResponse(responseCode = "200", description = "Producto creado correctamente")
    @PostMapping
    public EntityModel<Producto> createProducto(@RequestBody Producto producto) {
        Producto savedProducto = productoService.saveProducto(producto);
        return addLinksToProducto(savedProducto);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente por ID.")
    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PutMapping("/{idProducto}")
    public ResponseEntity<EntityModel<Producto>> updateProducto(
            @Parameter(description = "ID del producto a actualizar") @PathVariable int idProducto,
            @RequestBody Producto producto) {
        if (productoService.getProductoById(idProducto).isPresent()) {
            producto.setIdProducto(idProducto);
            Producto updatedProducto = productoService.saveProducto(producto);
            return ResponseEntity.ok(addLinksToProducto(updatedProducto));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID.")
    @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> deleteProducto(
            @Parameter(description = "ID del producto a eliminar") @PathVariable int idProducto) {
        if (productoService.getProductoById(idProducto).isPresent()) {
            productoService.deleteProducto(idProducto);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private EntityModel<Producto> addLinksToProducto(Producto producto) {
        return EntityModel.of(producto)
                .add(linkTo(ProductoController.class).slash(producto.getIdProducto()).withSelfRel())
                .add(linkTo(ProductoController.class).withRel(REL_PRODUCTOS));
    }
}
