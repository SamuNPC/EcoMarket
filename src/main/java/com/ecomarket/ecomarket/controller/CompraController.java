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

    // Constants for link relations
    private static final String REL_COMPRAS = "compras";
    private static final String REL_UPDATE = "update";
    private static final String REL_DELETE = "delete";
    private static final String REL_CLIENTE = "cliente";
    private static final String REL_DETALLES = "detalles";

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @Operation(summary = "Obtener todas las compras", description = "Devuelve una lista de todas las compras registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de compras obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> getAllCompras() {
        List<Compra> compras = compraService.getAllCompras();

        List<EntityModel<Compra>> comprasWithLinks = compras.stream()
                .map(this::addLinksToCompra)
                .toList();

        CollectionModel<EntityModel<Compra>> collectionModel = CollectionModel.of(comprasWithLinks);
        collectionModel.add(linkTo(CompraController.class).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener compra por ID", description = "Devuelve una compra según su ID.")
    @ApiResponse(responseCode = "200", description = "Compra encontrada")
    @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    @GetMapping("/{idCompra}")
    public ResponseEntity<EntityModel<Compra>> getCompraById(
            @Parameter(description = "ID de la compra") @PathVariable int idCompra) {
        Compra compra = compraService.getCompraById(idCompra);
        EntityModel<Compra> compraModel = addLinksToCompra(compra);
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
                .map(this::addLinksToCompra)
                .toList();

        CollectionModel<EntityModel<Compra>> collectionModel = CollectionModel.of(comprasWithLinks);
        collectionModel
                .add(linkTo(CompraController.class).slash("rango").slash(fechaInicio).slash(fechaFin).withSelfRel());
        collectionModel.add(linkTo(CompraController.class).withRel(REL_COMPRAS));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Crear una nueva compra", description = "Crea una nueva compra.")
    @ApiResponse(responseCode = "201", description = "Compra creada correctamente")
    @PostMapping
    public ResponseEntity<EntityModel<Compra>> createCompra(@RequestBody Compra compra) {
        Compra nuevaCompra = compraService.createCompra(compra);
        EntityModel<Compra> compraModel = addLinksToCompra(nuevaCompra);
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
        EntityModel<Compra> compraModel = addLinksToCompra(compraActualizada);
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

    // Helper method to add HATEOAS links to Compra
    private EntityModel<Compra> addLinksToCompra(Compra compra) {
        EntityModel<Compra> compraModel = EntityModel.of(compra)
                .add(linkTo(CompraController.class).slash(compra.getIdCompra()).withSelfRel())
                .add(linkTo(CompraController.class).slash(compra.getIdCompra()).withRel(REL_UPDATE))
                .add(linkTo(CompraController.class).slash(compra.getIdCompra()).withRel(REL_DELETE))
                .add(linkTo(CompraController.class).withRel(REL_COMPRAS));

        // Link to associated cliente
        if (compra.getCliente() != null) {
            compraModel.add(linkTo(ClienteController.class).slash(compra.getCliente().getRun()).withRel(REL_CLIENTE));
        }

        // Link to detalles
        compraModel.add(linkTo(DetalleController.class).withRel(REL_DETALLES));

        return compraModel;
    }
}
