package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Sucursal;
import com.ecomarket.ecomarket.service.SucursalService;
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

    @GetMapping
    public List<Sucursal> getAllSucursales() {
        return sucursalService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> getSucursalById(@PathVariable Integer id) {
        Optional<Sucursal> sucursal = sucursalService.findById(id);
        return sucursal.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sucursal createSucursal(@RequestBody Sucursal sucursal) {
        return sucursalService.save(sucursal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> updateSucursal(@PathVariable Integer id, @RequestBody Sucursal sucursalDetails) {
        Optional<Sucursal> sucursal = sucursalService.findById(id);
        if (sucursal.isPresent()) {
            Sucursal existingSucursal = sucursal.get();
            existingSucursal.setDireccionSucursal(sucursalDetails.getDireccionSucursal());
            existingSucursal.setComuna(sucursalDetails.getComuna());
            return ResponseEntity.ok(sucursalService.save(existingSucursal));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSucursal(@PathVariable Integer id) {
        if (sucursalService.findById(id).isPresent()) {
            sucursalService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
