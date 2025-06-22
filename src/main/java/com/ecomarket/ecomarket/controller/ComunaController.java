package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Comuna;
import com.ecomarket.ecomarket.service.ComunaService;
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
@RequestMapping("/api/comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @Operation(summary = "Obtener todas las comunas", description = "Devuelve una lista de todas las comunas registradas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de comunas obtenida correctamente")
    })
    @GetMapping
    public List<Comuna> getAllComunas() {
        return comunaService.findAll();
    }

    @Operation(summary = "Obtener comuna por ID", description = "Devuelve una comuna según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comuna encontrada"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Comuna> getComunaById(@Parameter(description = "ID de la comuna") @PathVariable Integer id) {
        Optional<Comuna> comuna = comunaService.findById(id);
        return comuna.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva comuna", description = "Crea una nueva comuna.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comuna creada correctamente")
    })
    @PostMapping
    public Comuna createComuna(@RequestBody Comuna comuna) {
        return comunaService.save(comuna);
    }

    @Operation(summary = "Actualizar comuna", description = "Actualiza los datos de una comuna existente por ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comuna actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Comuna> updateComuna(@Parameter(description = "ID de la comuna a actualizar") @PathVariable Integer id, @RequestBody Comuna comunaDetails) {
        Optional<Comuna> comuna = comunaService.findById(id);
        if (comuna.isPresent()) {
            Comuna existingComuna = comuna.get();
            existingComuna.setNombreComuna(comunaDetails.getNombreComuna());
            existingComuna.setRegion(comunaDetails.getRegion());
            return ResponseEntity.ok(comunaService.save(existingComuna));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar comuna", description = "Elimina una comuna por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Comuna eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComuna(@Parameter(description = "ID de la comuna a eliminar") @PathVariable Integer id) {
        if (comunaService.findById(id).isPresent()) {
            comunaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar comunas por nombre", description = "Busca comunas por su nombre.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de comunas encontradas")
    })
    @GetMapping("/buscar")
    public List<Comuna> findByNombre(@Parameter(description = "Nombre de la comuna a buscar") @RequestParam String nombre) {
        return comunaService.findByNombre(nombre);
    }

    @Operation(summary = "Buscar comunas por región", description = "Busca comunas según el ID de la región.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de comunas encontradas por región")
    })
    @GetMapping("/region/{idRegion}")
    public List<Comuna> findByRegionId(@PathVariable int idRegion) {
        return comunaService.findByRegionId(idRegion);
    }
}
