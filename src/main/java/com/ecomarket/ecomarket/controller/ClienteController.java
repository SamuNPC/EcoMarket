package com.ecomarket.ecomarket.controller;
import com.ecomarket.ecomarket.util.utils;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.repository.ClienteRepository;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes registrados.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida correctamente")
    })
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Operation(summary = "Obtener cliente por RUN", description = "Devuelve un cliente según su RUN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado o vacío si no existe")
    })
    @GetMapping("/{run}")
    public Cliente getClienteByRun(@Parameter(description = "RUN del cliente") @PathVariable String run) {
        List<Cliente> clientes = clienteRepository.findByRun(run);
        return clientes.isEmpty() ? null : clientes.get(0);
    }

    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente si el RUT es válido.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente creado correctamente"),
        @ApiResponse(responseCode = "400", description = "El RUT ingresado no es válido"),
        @ApiResponse(responseCode = "409", description = "Ya existe un cliente con ese RUN")
    })
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        if (!utils.esRutValido(cliente.getRun(), cliente.getDv())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUT ingresado no es válido");
        }
        if (clienteRepository.existsById(cliente.getRun())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un cliente con ese RUN");
        }
        return clienteRepository.save(cliente);
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente por RUN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente o null si no existe")
    })
    @PutMapping("/{run}")
    public Cliente updateCliente(@Parameter(description = "RUN del cliente a actualizar") @PathVariable String run, @RequestBody Cliente cliente) {
        if (clienteRepository.existsById(run)) {
            cliente.setRun(run);
            return clienteRepository.save(cliente);
        }
        return null;
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su RUN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente")
    })
    @DeleteMapping("/{run}")
    public void deleteCliente(@Parameter(description = "RUN del cliente a eliminar") @PathVariable String run) {
        clienteRepository.deleteById(run);
    }

}
