package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Detalle;
import com.ecomarket.ecomarket.service.DetalleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/detalles")
public class DetalleController {

    private final DetalleService detalleService;

    // Constants for link relations
    private static final String REL_DETALLES = "detalles";
    private static final String REL_UPDATE = "update";
    private static final String REL_DELETE = "delete";
    private static final String REL_COMPRA = "compra";

    public DetalleController(DetalleService detalleService) {
        this.detalleService = detalleService;
    }

    @Operation(summary = "Obtener todos los detalles", description = "Devuelve una lista de todos los detalles registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de detalles obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Detalle>>> getAllDetalles() {
        List<Detalle> detalles = detalleService.getAllDetalles();

        List<EntityModel<Detalle>> detallesWithLinks = detalles.stream()
                .map(this::addLinksToDetalle)
                .toList();

        CollectionModel<EntityModel<Detalle>> collectionModel = CollectionModel.of(detallesWithLinks);
        collectionModel.add(linkTo(DetalleController.class).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener detalle por ID", description = "Devuelve un detalle según su ID.")
    @ApiResponse(responseCode = "200", description = "Detalle encontrado")
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    @GetMapping("/{idDetalle}")
    public ResponseEntity<EntityModel<Detalle>> getDetalleById(
            @Parameter(description = "ID del detalle") @PathVariable int idDetalle) {
        Detalle detalle = detalleService.getDetalleById(idDetalle);
        EntityModel<Detalle> detalleModel = addLinksToDetalle(detalle);
        return ResponseEntity.ok(detalleModel);
    }

    @Operation(summary = "Crear un nuevo detalle", description = "Crea un nuevo detalle.")
    @ApiResponse(responseCode = "201", description = "Detalle creado correctamente")
    @PostMapping
    public ResponseEntity<EntityModel<Detalle>> createDetalle(@RequestBody Detalle detalle) {
        Detalle nuevoDetalle = detalleService.createDetalle(detalle);
        EntityModel<Detalle> detalleModel = addLinksToDetalle(nuevoDetalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleModel);
    }

    @Operation(summary = "Actualizar detalle", description = "Actualiza los datos de un detalle existente por ID.")
    @ApiResponse(responseCode = "200", description = "Detalle actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    @PutMapping("/{idDetalle}")
    public ResponseEntity<EntityModel<Detalle>> updateDetalle(
            @Parameter(description = "ID del detalle a actualizar") @PathVariable int idDetalle,
            @RequestBody Detalle detalle) {
        Detalle detalleActualizado = detalleService.updateDetalle(idDetalle, detalle);
        EntityModel<Detalle> detalleModel = addLinksToDetalle(detalleActualizado);
        return ResponseEntity.ok(detalleModel);
    }

    @Operation(summary = "Eliminar detalle", description = "Elimina un detalle por su ID.")
    @ApiResponse(responseCode = "204", description = "Detalle eliminado correctamente")
    @DeleteMapping("/{idDetalle}")
    public ResponseEntity<Void> deleteDetalle(
            @Parameter(description = "ID del detalle a eliminar") @PathVariable int idDetalle) {
        detalleService.deleteDetalle(idDetalle);
        return ResponseEntity.noContent().build();
    }

    // Helper method to add HATEOAS links to Detalle
    private EntityModel<Detalle> addLinksToDetalle(Detalle detalle) {
        EntityModel<Detalle> detalleModel = EntityModel.of(detalle)
                .add(linkTo(DetalleController.class).slash(detalle.getIdDetalle()).withSelfRel())
                .add(linkTo(DetalleController.class).slash(detalle.getIdDetalle()).withRel(REL_UPDATE))
                .add(linkTo(DetalleController.class).slash(detalle.getIdDetalle()).withRel(REL_DELETE))
                .add(linkTo(DetalleController.class).withRel(REL_DETALLES));

        // Link to associated compra
        if (detalle.getCompra() != null) {
            detalleModel
                    .add(linkTo(CompraController.class).slash(detalle.getCompra().getIdCompra()).withRel(REL_COMPRA));
        }

        return detalleModel;
    }
}
