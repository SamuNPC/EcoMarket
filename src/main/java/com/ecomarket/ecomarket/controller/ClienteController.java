package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.service.ClienteService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> getAllClientes() {
        List<Cliente> clientes = clienteService.getAllClientes();

        List<EntityModel<Cliente>> clientesWithLinks = clientes.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Cliente>> collectionModel = CollectionModel.of(clientesWithLinks);
        collectionModel.add(linkTo(methodOn(ClienteController.class).getAllClientes()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener cliente por RUN", description = "Devuelve un cliente según su RUN.")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    @GetMapping("/{run}")
    public ResponseEntity<EntityModel<Cliente>> getClienteByRun(
            @Parameter(description = "RUN del cliente") @PathVariable String run) {
        Cliente cliente = clienteService.getClienteByRun(run);
        EntityModel<Cliente> clienteModel = toEntityModel(cliente);
        return ResponseEntity.ok(clienteModel);
    }

    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente si el RUT es válido.")
    @ApiResponse(responseCode = "201", description = "Cliente creado correctamente")
    @ApiResponse(responseCode = "400", description = "El RUT ingresado no es válido")
    @ApiResponse(responseCode = "409", description = "Ya existe un cliente con ese RUN")
    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> createCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.createCliente(cliente);
        EntityModel<Cliente> clienteModel = toEntityModel(nuevoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteModel);
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente por RUN.")
    @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    @PutMapping("/{run}")
    public ResponseEntity<EntityModel<Cliente>> updateCliente(
            @Parameter(description = "RUN del cliente a actualizar") @PathVariable String run,
            @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.updateCliente(run, cliente);
        EntityModel<Cliente> clienteModel = toEntityModel(clienteActualizado);
        return ResponseEntity.ok(clienteModel);
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su RUN.")
    @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente")
    @DeleteMapping("/{run}")
    public ResponseEntity<Void> deleteCliente(
            @Parameter(description = "RUN del cliente a eliminar") @PathVariable String run) {
        clienteService.deleteCliente(run);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para crear EntityModel con enlaces HATEOAS
    private EntityModel<Cliente> toEntityModel(Cliente cliente) {
        return EntityModel.of(cliente)
                .add(linkTo(methodOn(ClienteController.class).getClienteByRun(cliente.getRun())).withSelfRel())
                .add(linkTo(methodOn(ClienteController.class).updateCliente(cliente.getRun(), cliente))
                        .withRel("update"))
                .add(linkTo(methodOn(ClienteController.class).deleteCliente(cliente.getRun())).withRel("delete"))
                .add(linkTo(methodOn(ClienteController.class).getAllClientes()).withRel("all-clientes"));
    }

}
