package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Compra;
import com.ecomarket.ecomarket.service.CompraService;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @Operation(summary = "Obtener todas las compras", description = "Devuelve una lista de todas las compras registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de compras obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> getAllCompras() {
        List<Compra> compras = compraService.getAllCompras();

        List<EntityModel<Compra>> comprasWithLinks = compras.stream()
                .map(this::toEntityModel)
                .toList();

        CollectionModel<EntityModel<Compra>> collectionModel = CollectionModel.of(comprasWithLinks);
        collectionModel.add(linkTo(methodOn(CompraController.class).getAllCompras()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener compra por ID", description = "Devuelve una compra según su ID.")
    @ApiResponse(responseCode = "200", description = "Compra encontrada")
    @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    @GetMapping("/{idCompra}")
    public ResponseEntity<EntityModel<Compra>> getCompraById(
            @Parameter(description = "ID de la compra") @PathVariable int idCompra) {
        Compra compra = compraService.getCompraById(idCompra);
        EntityModel<Compra> compraModel = toEntityModel(compra);
        return ResponseEntity.ok(compraModel);
    }

    @Operation(summary = "Obtener compras por rango de fechas", description = "Devuelve una lista de compras entre dos fechas (formato: yyyy-MM-dd).")
    @ApiResponse(responseCode = "200", description = "Lista de compras obtenida correctamente")
    @ApiResponse(responseCode = "400", description = "Formato de fecha inválido")
    @GetMapping("/rango/{fechaInicio}/{fechaFin}")
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> getComprasByFecha(
            @Parameter(description = "Fecha de inicio (yyyy-MM-dd)") @PathVariable String fechaInicio,
            @Parameter(description = "Fecha de fin (yyyy-MM-dd)") @PathVariable String fechaFin) {
        List<Compra> compras = compraService.getComprasByFecha(fechaInicio, fechaFin);

        List<EntityModel<Compra>> comprasWithLinks = compras.stream()
                .map(this::toEntityModel)
                .toList();

        CollectionModel<EntityModel<Compra>> collectionModel = CollectionModel.of(comprasWithLinks);
        collectionModel
                .add(linkTo(methodOn(CompraController.class).getComprasByFecha(fechaInicio, fechaFin)).withSelfRel());
        collectionModel.add(linkTo(methodOn(CompraController.class).getAllCompras()).withRel("all-compras"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Crear una nueva compra", description = "Crea una nueva compra.")
    @ApiResponse(responseCode = "201", description = "Compra creada correctamente")
    @PostMapping
    public ResponseEntity<EntityModel<Compra>> createCompra(@RequestBody Compra compra) {
        Compra nuevaCompra = compraService.createCompra(compra);
        EntityModel<Compra> compraModel = toEntityModel(nuevaCompra);
        return ResponseEntity.status(HttpStatus.CREATED).body(compraModel);
    }

    @Operation(summary = "Actualizar compra", description = "Actualiza los datos de una compra existente por ID.")
    @ApiResponse(responseCode = "200", description = "Compra actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    @PutMapping("/{idCompra}")
    public ResponseEntity<EntityModel<Compra>> updateCompra(
            @Parameter(description = "ID de la compra a actualizar") @PathVariable Integer idCompra,
            @RequestBody Compra compra) {
        Compra compraActualizada = compraService.updateCompra(idCompra, compra);
        EntityModel<Compra> compraModel = toEntityModel(compraActualizada);
        return ResponseEntity.ok(compraModel);
    }

    @Operation(summary = "Eliminar compra", description = "Elimina una compra por su ID.")
    @ApiResponse(responseCode = "204", description = "Compra eliminada correctamente")
    @DeleteMapping("/{idCompra}")
    public ResponseEntity<Void> deleteCompra(
            @Parameter(description = "ID de la compra a eliminar") @PathVariable Integer idCompra) {
        compraService.deleteCompra(idCompra);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para crear EntityModel con enlaces HATEOAS
    private EntityModel<Compra> toEntityModel(Compra compra) {
        EntityModel<Compra> compraModel = EntityModel.of(compra)
                .add(linkTo(methodOn(CompraController.class).getCompraById(compra.getIdCompra())).withSelfRel())
                .add(linkTo(methodOn(CompraController.class).updateCompra(compra.getIdCompra(), compra))
                        .withRel("update"))
                .add(linkTo(methodOn(CompraController.class).deleteCompra(compra.getIdCompra())).withRel("delete"))
                .add(linkTo(methodOn(CompraController.class).getAllCompras()).withRel("all-compras"));

        // Enlace al cliente asociado
        if (compra.getCliente() != null) {
            compraModel.add(linkTo(methodOn(ClienteController.class).getClienteByRun(compra.getCliente().getRun()))
                    .withRel("cliente"));
        }

        // Enlace a los detalles (usando el endpoint general de detalles)
        compraModel.add(linkTo(methodOn(DetalleController.class).getAllDetalles()).withRel("detalles"));

        return compraModel;
    }
}
