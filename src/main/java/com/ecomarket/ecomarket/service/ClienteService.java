package com.ecomarket.ecomarket.service;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.repository.ClienteRepository;
import com.ecomarket.ecomarket.util.utils;

import java.util.List;

import org.springframework.stereotype.Service;
import com.ecomarket.ecomarket.exception.BadRequestException;
import com.ecomarket.ecomarket.exception.ResourceAlreadyExistsException;
import com.ecomarket.ecomarket.exception.ResourceNotFoundException;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente getClienteByRun(String run) {
        List<Cliente> clientes = clienteRepository.findByRun(run);
        if (clientes.isEmpty()) {
            throw new ResourceNotFoundException("Cliente", "RUN", run);
        }
        return clientes.get(0);
    }

    public Cliente createCliente(Cliente cliente) {
        if (!utils.esRutValido(cliente.getRun(), cliente.getDv())) {
            throw new BadRequestException("El RUT ingresado no es válido");
        }
        if (clienteRepository.existsById(cliente.getRun())) {
            throw new ResourceAlreadyExistsException("Cliente", "RUN", cliente.getRun());
        }
        return clienteRepository.save(cliente);
    }

    public Cliente updateCliente(String run, Cliente cliente) {
        if (!clienteRepository.existsById(run)) {
            throw new ResourceNotFoundException("Cliente", "RUN", run);
        }
        cliente.setRun(run);
        return clienteRepository.save(cliente);
    }

    public void deleteCliente(String run) {
        clienteRepository.deleteById(run);
    }
}
