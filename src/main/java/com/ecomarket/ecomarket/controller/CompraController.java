package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Compra;
import com.ecomarket.ecomarket.repository.CompraRepository;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraRepository compraRepository;

    public CompraController(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Operation(summary = "Obtener todas las compras", description = "Devuelve una lista de todas las compras registradas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de compras obtenida correctamente")
    })
    @GetMapping
    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    @Operation(summary = "Obtener compra por ID", description = "Devuelve una compra según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Compra encontrada"),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @GetMapping("/{idCompra}")
    public ResponseEntity<Compra> getCompraById(@Parameter(description = "ID de la compra") @PathVariable int idCompra) {
        List<Compra> compras = compraRepository.findByIdCompra(idCompra);
        if (compras != null && !compras.isEmpty()) {
            return ResponseEntity.ok(compras.get(0));
        } else {
            return ResponseEntity.notFound().build();
        }
    
    }
    @Operation(summary = "Obtener compras por rango de fechas", description = "Devuelve una lista de compras entre dos fechas (formato: yyyy-MM-dd).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de compras obtenida correctamente"),
        @ApiResponse(responseCode = "400", description = "Formato de fecha inválido")
    })
    @GetMapping("/rango/{fechaInicio}/{fechaFin}")
    public List<Compra> getComprasByFecha(
            @Parameter(description = "Fecha de inicio (yyyy-MM-dd)") @PathVariable String fechaInicio,
            @Parameter(description = "Fecha de fin (yyyy-MM-dd)") @PathVariable String fechaFin) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaInicioDate;
        Date fechaFinDate;
        try {
            fechaInicioDate = dateFormat.parse(fechaInicio);
            fechaFinDate = dateFormat.parse(fechaFin);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de fecha inválido");
        }
        return compraRepository.findByFechaCompraBetween(fechaInicioDate, fechaFinDate);
    }
    

    @Operation(summary = "Crear una nueva compra", description = "Crea una nueva compra.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Compra creada correctamente")
    })
    @PostMapping
    public Compra createCompra(@RequestBody Compra compra) {
        return compraRepository.save(compra);
    }

    @Operation(summary = "Actualizar compra", description = "Actualiza los datos de una compra existente por ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Compra actualizada correctamente o null si no existe")
    })
    @PutMapping("/{idCompra}")
    public Compra updateCompra(@Parameter(description = "ID de la compra a actualizar") @PathVariable Integer idCompra, @RequestBody Compra compra) {
        if (compraRepository.existsById(idCompra)) {
            compra.setIdCompra(idCompra.intValue());
            return compraRepository.save(compra);
        }
        return null;
    }

    @Operation(summary = "Eliminar compra", description = "Elimina una compra por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Compra eliminada correctamente")
    })
    @DeleteMapping("/{idCompra}")
    public void deleteCompra(@Parameter(description = "ID de la compra a eliminar") @PathVariable Integer idCompra) {
        compraRepository.deleteById(idCompra);
    }
}
