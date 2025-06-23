package com.ecomarket.ecomarket.service;

import com.ecomarket.ecomarket.model.Detalle;
import com.ecomarket.ecomarket.repository.DetalleRepository;
import com.ecomarket.ecomarket.exception.ResourceNotFoundException;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DetalleService {

    private final DetalleRepository detalleRepository;

    public DetalleService(DetalleRepository detalleRepository) {
        this.detalleRepository = detalleRepository;
    }

    public List<Detalle> getAllDetalles() {
        return detalleRepository.findAll();
    }

    public Detalle getDetalleById(int idDetalle) {
        List<Detalle> detalles = detalleRepository.findById(idDetalle);
        if (detalles.isEmpty()) {
            throw new ResourceNotFoundException("Detalle", "ID", idDetalle);
        }
        return detalles.get(0);
    }

    public Detalle createDetalle(Detalle detalle) {
        return detalleRepository.save(detalle);
    }

    public Detalle updateDetalle(int idDetalle, Detalle detalle) {
        if (!detalleRepository.existsById(idDetalle)) {
            throw new ResourceNotFoundException("Detalle", "ID", idDetalle);
        }
        detalle.setIdDetalle(idDetalle);
        return detalleRepository.save(detalle);
    }

    public void deleteDetalle(int idDetalle) {
        detalleRepository.deleteById(idDetalle);
    }
}