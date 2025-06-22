package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Sucursal;
import com.ecomarket.ecomarket.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @Operation(summary = "Obtener todas las sucursales", description = "Devuelve una lista de todas las sucursales registradas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida correctamente")
    })
    @GetMapping
    public List<Sucursal> getAllSucursales() {
        return sucursalService.findAll();
    }

    @Operation(summary = "Obtener sucursal por ID", description = "Devuelve una sucursal según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> getSucursalById(@Parameter(description = "ID de la sucursal") @PathVariable Integer id) {
        Optional<Sucursal> sucursal = sucursalService.findById(id);
        return sucursal.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva sucursal", description = "Crea una nueva sucursal.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal creada correctamente")
    })
    @PostMapping
    public Sucursal createSucursal(@RequestBody Sucursal sucursal) {
        return sucursalService.save(sucursal);
    }

    @Operation(summary = "Actualizar sucursal", description = "Actualiza los datos de una sucursal existente por ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> updateSucursal(@Parameter(description = "ID de la sucursal a actualizar") @PathVariable Integer id, @RequestBody Sucursal sucursalDetails) {
        Optional<Sucursal> sucursal = sucursalService.findById(id);
        if (sucursal.isPresent()) {
            Sucursal existingSucursal = sucursal.get();
            existingSucursal.setDireccionSucursal(sucursalDetails.getDireccionSucursal());
            existingSucursal.setComuna(sucursalDetails.getComuna());
            return ResponseEntity.ok(sucursalService.save(existingSucursal));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Sucursal eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSucursal(@Parameter(description = "ID de la sucursal a eliminar") @PathVariable Integer id) {
        if (sucursalService.findById(id).isPresent()) {
            sucursalService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
