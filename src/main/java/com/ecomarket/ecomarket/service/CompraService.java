package com.ecomarket.ecomarket.service;

import com.ecomarket.ecomarket.model.Compra;
import com.ecomarket.ecomarket.repository.CompraRepository;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;
import com.ecomarket.ecomarket.exception.BadRequestException;
import com.ecomarket.ecomarket.exception.ResourceNotFoundException;

@Service
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    public Compra getCompraById(int idCompra) {
        return compraRepository.findById(idCompra)
                .orElseThrow(() -> new ResourceNotFoundException("Compra", "ID", idCompra));
    }

    public List<Compra> getComprasByFecha(String fechaInicio, String fechaFin) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaInicioDate;
        Date fechaFinDate;
        try {
            fechaInicioDate = dateFormat.parse(fechaInicio);
            fechaFinDate = dateFormat.parse(fechaFin);
        } catch (Exception e) {
            throw new BadRequestException("Formato de fecha inválido");
        }
        return compraRepository.findByFechaCompraBetween(fechaInicioDate, fechaFinDate);
    }

    public Compra createCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    public Compra updateCompra(Integer idCompra, Compra compra) {
        if (!compraRepository.existsById(idCompra)) {
            throw new ResourceNotFoundException("Compra", "ID", idCompra);
        }
        compra.setIdCompra(idCompra.intValue());
        return compraRepository.save(compra);
    }

    public void deleteCompra(Integer idCompra) {
        compraRepository.deleteById(idCompra);
    }
}
