package com.ecomarket.ecomarket.controller;
import com.ecomarket.ecomarket.util.utils;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.repository.ClienteRepository;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{run}")
    public Cliente getClienteByRun(@PathVariable String run) {
        List<Cliente> clientes = clienteRepository.findByRun(run);
        return clientes.isEmpty() ? null : clientes.get(0);
    }

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        if (utils.esRutValido(cliente.getRun(), cliente.getDv()) == true){
            return clienteRepository.save(cliente);
        }
        throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST, "El RUT ingresado no es válido"
    );
    }

    @PutMapping("/{run}")
    public Cliente updateCliente(@PathVariable String run, @RequestBody Cliente cliente) {
        if (clienteRepository.existsById(run)) {
            cliente.setRun(run);
            return clienteRepository.save(cliente);
        }
        return null;
    }

    @DeleteMapping("/{run}")
    public void deleteCliente(@PathVariable String run) {
        clienteRepository.deleteById(run);
    }

}
