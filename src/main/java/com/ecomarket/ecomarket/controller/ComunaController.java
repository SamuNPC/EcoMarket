package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Comuna;
import com.ecomarket.ecomarket.service.ComunaService;
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

    @GetMapping
    public List<Comuna> getAllComunas() {
        return comunaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comuna> getComunaById(@PathVariable Integer id) {
        Optional<Comuna> comuna = comunaService.findById(id);
        return comuna.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Comuna createComuna(@RequestBody Comuna comuna) {
        return comunaService.save(comuna);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comuna> updateComuna(@PathVariable Integer id, @RequestBody Comuna comunaDetails) {
        Optional<Comuna> comuna = comunaService.findById(id);
        if (comuna.isPresent()) {
            Comuna existingComuna = comuna.get();
            existingComuna.setNombreComuna(comunaDetails.getNombreComuna());
            existingComuna.setRegion(comunaDetails.getRegion());
            return ResponseEntity.ok(comunaService.save(existingComuna));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComuna(@PathVariable Integer id) {
        if (comunaService.findById(id).isPresent()) {
            comunaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public List<Comuna> findByNombre(@RequestParam String nombre) {
        return comunaService.findByNombre(nombre);
    }

    @GetMapping("/region/{idRegion}")
    public List<Comuna> findByRegionId(@PathVariable int idRegion) {
        return comunaService.findByRegionId(idRegion);
    }
}
