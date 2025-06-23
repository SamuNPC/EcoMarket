package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Region;
import com.ecomarket.ecomarket.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regiones")
@CrossOrigin(origins = "*")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    // GET /api/regiones - Obtener todas las regiones
    @Operation(summary = "Obtener todas las regiones", description = "Devuelve una lista de todas las regiones registradas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Region>> getAllRegiones() {
        List<Region> regiones = regionService.getAllRegiones();
        return ResponseEntity.ok(regiones);
    }

    // GET /api/regiones/{id} - Obtener región por ID
    @Operation(summary = "Obtener región por ID", description = "Devuelve una región según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Región encontrada"),
        @ApiResponse(responseCode = "404", description = "Región no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@Parameter(description = "ID de la región") @PathVariable int id) {
        Optional<Region> region = regionService.getRegionById(id);
        return region.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/regiones - Crear nueva región
    @Operation(summary = "Crear una nueva región", description = "Crea una nueva región.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Región creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear la región")
    })
    @PostMapping
    public ResponseEntity<Region> createRegion(@RequestBody Region region) {
        try {
            Region savedRegion = regionService.saveRegion(region);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRegion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // PUT /api/regiones/{id} - Actualizar región existente
    @Operation(summary = "Actualizar región", description = "Actualiza los datos de una región existente por ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Región actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Región no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Region> updateRegion(@Parameter(description = "ID de la región a actualizar") @PathVariable int id, @RequestBody Region regionDetails) {
        Optional<Region> optionalRegion = regionService.getRegionById(id);

        if (optionalRegion.isPresent()) {
            Region region = optionalRegion.get();
            region.setNombreRegion(regionDetails.getNombreRegion());

            Region updatedRegion = regionService.saveRegion(region);
            return ResponseEntity.ok(updatedRegion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/regiones/{id} - Eliminar región
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable int id) {
        Optional<Region> region = regionService.getRegionById(id);

        if (region.isPresent()) {
            regionService.deleteRegion(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/regiones/search?nombre={nombre} - Buscar regiones por nombre
    @GetMapping("/search")
    public ResponseEntity<List<Region>> searchRegionesByNombre(@RequestParam String nombre) {
        List<Region> regiones = regionService.findByNombre(nombre);
        return ResponseEntity.ok(regiones);
    }
}
