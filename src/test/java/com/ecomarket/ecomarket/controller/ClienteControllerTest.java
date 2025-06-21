package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente1;
    private Cliente cliente2;
    private List<Cliente> clientesList;

    @BeforeEach
    void setUp() {
        cliente1 = new Cliente("12345678", '9', "Juan", "Pérez");
        cliente2 = new Cliente("87654321", '0', "María", "González");
        clientesList = Arrays.asList(cliente1, cliente2);
    }

    @Test
    void testGetAllClientes() throws Exception {
        // Given
        when(clienteRepository.findAll()).thenReturn(clientesList);

        // When & Then
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].run", is("12345678")))
                .andExpect(jsonPath("$[0].dv", is("9")))
                .andExpect(jsonPath("$[0].nombres", is("Juan")))
                .andExpect(jsonPath("$[0].apellidos", is("Pérez")))
                .andExpect(jsonPath("$[1].run", is("87654321")))
                .andExpect(jsonPath("$[1].dv", is("0")))
                .andExpect(jsonPath("$[1].nombres", is("María")))
                .andExpect(jsonPath("$[1].apellidos", is("González")));

        verify(clienteRepository).findAll();
    }

    @Test
    void testGetAllClientesListaVacia() throws Exception {
        // Given
        when(clienteRepository.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(clienteRepository).findAll();
    }

    @Test
    void testGetClienteByRunExistente() throws Exception {
        // Given
        String run = "12345678";
        when(clienteRepository.findByRun(run)).thenReturn(Arrays.asList(cliente1));

        // When & Then
        mockMvc.perform(get("/api/clientes/{run}", run))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.run", is("12345678")))
                .andExpect(jsonPath("$.dv", is("9")))
                .andExpect(jsonPath("$.nombres", is("Juan")))
                .andExpect(jsonPath("$.apellidos", is("Pérez")));

        verify(clienteRepository).findByRun(run);
    }

    @Test
    void testGetClienteByRunNoExistente() throws Exception {
        // Given
        String run = "99999999";
        when(clienteRepository.findByRun(run)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/clientes/{run}", run))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(clienteRepository).findByRun(run);
    }

    @Test
    void testCreateCliente() throws Exception {
        // Given
        Cliente nuevoCliente = new Cliente("11111111", '1', "Carlos", "Rodríguez");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(nuevoCliente);

        // When & Then
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoCliente)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.run", is("11111111")))
                .andExpect(jsonPath("$.dv", is("1")))
                .andExpect(jsonPath("$.nombres", is("Carlos")))
                .andExpect(jsonPath("$.apellidos", is("Rodríguez")));

        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testUpdateClienteExistente() throws Exception {
        // Given
        String run = "12345678";
        Cliente clienteActualizado = new Cliente(run, '9', "Juan Carlos", "Pérez López");
        when(clienteRepository.existsById(run)).thenReturn(true);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        // When & Then
        mockMvc.perform(put("/api/clientes/{run}", run)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.run", is("12345678")))
                .andExpect(jsonPath("$.nombres", is("Juan Carlos")))
                .andExpect(jsonPath("$.apellidos", is("Pérez López")));

        verify(clienteRepository).existsById(run);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testUpdateClienteNoExistente() throws Exception {
        // Given
        String run = "99999999";
        Cliente cliente = new Cliente(run, '1', "No", "Existe");
        when(clienteRepository.existsById(run)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/clientes/{run}", run)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(clienteRepository).existsById(run);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testDeleteCliente() throws Exception {
        // Given
        String run = "12345678";
        doNothing().when(clienteRepository).deleteById(run);

        // When & Then
        mockMvc.perform(delete("/api/clientes/{run}", run))
                .andExpect(status().isOk());

        verify(clienteRepository).deleteById(run);
    }

    @Test
    void testCreateClienteConDatosIncompletos() throws Exception {
        // Given
        Cliente clienteIncompleto = new Cliente();
        clienteIncompleto.setRun("11111111");
        // Sin DV, nombres y apellidos

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteIncompleto);

        // When & Then
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteIncompleto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.run", is("11111111")));

        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testGetClienteByRunConCaracteresEspeciales() throws Exception {
        // Given
        String runEspecial = "12345678-9";
        when(clienteRepository.findByRun(runEspecial)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/clientes/{run}", runEspecial))
                .andExpect(status().isOk());

        verify(clienteRepository).findByRun(runEspecial);
    }
}
