package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.model.Compra;
import com.ecomarket.ecomarket.model.Sucursal;
import com.ecomarket.ecomarket.repository.CompraRepository;
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
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CompraController.class)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraRepository compraRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Compra compra1;
    private Compra compra2;
    private Cliente cliente;
    private Sucursal sucursal;
    private List<Compra> comprasList;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("12345678", '9', "Juan", "Pérez");

        sucursal = new Sucursal();
        sucursal.setIdSucursal(1);
        sucursal.setDireccionSucursal("Calle Principal 123");

        compra1 = new Compra();
        compra1.setIdCompra(1);
        compra1.setFechaCompra(new Date());
        compra1.setNumeroFactura("F001");
        compra1.setCliente(cliente);
        compra1.setSucursal(sucursal);

        compra2 = new Compra();
        compra2.setIdCompra(2);
        compra2.setFechaCompra(new Date());
        compra2.setNumeroFactura("F002");
        compra2.setCliente(cliente);
        compra2.setSucursal(sucursal);

        comprasList = Arrays.asList(compra1, compra2);
    }

    @Test
    void testGetAllCompras() throws Exception {
        // Given
        when(compraRepository.findAll()).thenReturn(comprasList);

        // When & Then
        mockMvc.perform(get("/api/compras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idCompra", is(1)))
                .andExpect(jsonPath("$[0].numeroFactura", is("F001")))
                .andExpect(jsonPath("$[1].idCompra", is(2)))
                .andExpect(jsonPath("$[1].numeroFactura", is("F002")));

        verify(compraRepository).findAll();
    }

    @Test
    void testGetAllComprasListaVacia() throws Exception {
        // Given
        when(compraRepository.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/compras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(compraRepository).findAll();
    }

    @Test
    void testGetCompraByIdExistente() throws Exception {
        // Given
        int idCompra = 1;
        when(compraRepository.findByIdCompra(idCompra)).thenReturn(Arrays.asList(compra1));

        // When & Then
        mockMvc.perform(get("/api/compras/{idCompra}", idCompra))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idCompra", is(1)))
                .andExpect(jsonPath("$.numeroFactura", is("F001")));

        verify(compraRepository).findByIdCompra(idCompra);
    }

    @Test
    void testGetCompraByIdNoExistente() throws Exception {
        // Given
        int idCompra = 999;
        when(compraRepository.findByIdCompra(idCompra)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/compras/{idCompra}", idCompra))
                .andExpect(status().isNotFound());

        verify(compraRepository).findByIdCompra(idCompra);
    }

    @Test
    void testGetCompraByIdListaVacia() throws Exception {
        // Given
        int idCompra = 999;
        when(compraRepository.findByIdCompra(idCompra)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/compras/{idCompra}", idCompra))
                .andExpect(status().isNotFound());

        verify(compraRepository).findByIdCompra(idCompra);
    }

    @Test
    void testCreateCompra() throws Exception {
        // Given
        Compra nuevaCompra = new Compra();
        nuevaCompra.setIdCompra(3);
        nuevaCompra.setFechaCompra(new Date());
        nuevaCompra.setNumeroFactura("F003");
        nuevaCompra.setCliente(cliente);
        nuevaCompra.setSucursal(sucursal);

        when(compraRepository.save(any(Compra.class))).thenReturn(nuevaCompra);

        // When & Then
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaCompra)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idCompra", is(3)))
                .andExpect(jsonPath("$.numeroFactura", is("F003")));

        verify(compraRepository).save(any(Compra.class));
    }

    @Test
    void testUpdateCompraExistente() throws Exception {
        // Given
        Integer idCompra = 1;
        Compra compraActualizada = new Compra();
        compraActualizada.setIdCompra(idCompra);
        compraActualizada.setNumeroFactura("F001-UPDATED");
        compraActualizada.setFechaCompra(new Date());
        compraActualizada.setCliente(cliente);
        compraActualizada.setSucursal(sucursal);

        when(compraRepository.existsById(idCompra)).thenReturn(true);
        when(compraRepository.save(any(Compra.class))).thenReturn(compraActualizada);

        // When & Then
        mockMvc.perform(put("/api/compras/{idCompra}", idCompra)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compraActualizada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idCompra", is(1)))
                .andExpect(jsonPath("$.numeroFactura", is("F001-UPDATED")));

        verify(compraRepository).existsById(idCompra);
        verify(compraRepository).save(any(Compra.class));
    }

    @Test
    void testUpdateCompraNoExistente() throws Exception {
        // Given
        Integer idCompra = 999;
        Compra compra = new Compra();
        compra.setIdCompra(idCompra);
        compra.setNumeroFactura("F999");

        when(compraRepository.existsById(idCompra)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/compras/{idCompra}", idCompra)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compra)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(compraRepository).existsById(idCompra);
        verify(compraRepository, never()).save(any(Compra.class));
    }

    @Test
    void testDeleteCompra() throws Exception {
        // Given
        Integer idCompra = 1;
        doNothing().when(compraRepository).deleteById(idCompra);

        // When & Then
        mockMvc.perform(delete("/api/compras/{idCompra}", idCompra))
                .andExpect(status().isOk());

        verify(compraRepository).deleteById(idCompra);
    }

    @Test
    void testCreateCompraConDatosCompletos() throws Exception {
        // Given
        Compra compraCompleta = new Compra();
        compraCompleta.setIdCompra(100);
        compraCompleta.setFechaCompra(new Date());
        compraCompleta.setNumeroFactura("FACT-2023-100");
        compraCompleta.setCliente(cliente);
        compraCompleta.setSucursal(sucursal);

        when(compraRepository.save(any(Compra.class))).thenReturn(compraCompleta);

        // When & Then
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compraCompleta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCompra", is(100)))
                .andExpect(jsonPath("$.numeroFactura", is("FACT-2023-100")));

        verify(compraRepository).save(any(Compra.class));
    }
}
