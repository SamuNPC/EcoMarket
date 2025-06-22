package com.ecomarket.ecomarket.controller;


import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.model.Detalle;
import com.ecomarket.ecomarket.repository.DetalleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detalles")
public class DetalleController {

    @Autowired
    private DetalleRepository detalleRepository;

    @Operation(summary = "Obtener todos los detalles", description = "Devuelve una lista de todos los detalles registrados.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de detalles obtenida correctamente")
    })
    @GetMapping
    public List<Detalle> getAllDetalles() {
        return detalleRepository.findAll();
    }

    @Operation(summary = "Obtener detalle por ID", description = "Devuelve un detalle según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle encontrado o null si no existe")
    })
    @GetMapping("/{idDetalle}")
    public Detalle getDetalleById(@Parameter(description = "ID del detalle") @PathVariable int idDetalle) {
        List<Detalle> detalles = detalleRepository.findById(idDetalle);
        return detalles.isEmpty() ? null : detalles.get(0);
    }

    @Operation(summary = "Crear un nuevo detalle", description = "Crea un nuevo detalle.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle creado correctamente")
    })
    @PostMapping
    public Detalle createDetalle(@RequestBody Detalle detalle) {
        return detalleRepository.save(detalle);
    }

    @Operation(summary = "Actualizar detalle", description = "Actualiza los datos de un detalle existente por ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle actualizado correctamente o null si no existe")
    })
    @PutMapping("/{idDetalle}")
    public Detalle updateDetalle(@Parameter(description = "ID del detalle a actualizar") @PathVariable int idDetalle, @RequestBody Detalle detalle) {
        if (detalleRepository.existsById(idDetalle)) {
            detalle.setIdDetalle(idDetalle);
            return detalleRepository.save(detalle);
        }
        return null;
    }

    @Operation(summary = "Eliminar detalle", description = "Elimina un detalle por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle eliminado correctamente")
    })
    @DeleteMapping("/{idDetalle}")
    public void deleteDetalle(@Parameter(description = "ID del detalle a eliminar") @PathVariable int idDetalle) {
        detalleRepository.deleteById(idDetalle);
    }
}
