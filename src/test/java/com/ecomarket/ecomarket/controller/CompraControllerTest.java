package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.model.Compra;
import com.ecomarket.ecomarket.model.Sucursal;
import com.ecomarket.ecomarket.service.CompraService;
import com.ecomarket.ecomarket.exception.ResourceNotFoundException;
import com.ecomarket.ecomarket.exception.BadRequestException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CompraController.class)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

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
        sucursal.setDireccionSucursal("Centro");

        compra1 = new Compra();
        compra1.setIdCompra(1);
        compra1.setFechaCompra(new Date());
        compra1.setNumeroFactura("001001");
        compra1.setCliente(cliente);
        compra1.setSucursal(sucursal);

        compra2 = new Compra();
        compra2.setIdCompra(2);
        compra2.setFechaCompra(new Date());
        compra2.setNumeroFactura("001002");
        compra2.setCliente(cliente);
        compra2.setSucursal(sucursal);

        comprasList = Arrays.asList(compra1, compra2);
    }

    @Test
    void testGetAllCompras() throws Exception {
        System.out.println("=== INICIANDO TEST: testGetAllCompras ===");
        System.out.flush();
        try {
            // Given
            when(compraService.getAllCompras()).thenReturn(comprasList);

            // When & Then
            mockMvc.perform(get("/api/compras"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$._embedded.compraList", hasSize(2)))
                    .andExpect(jsonPath("$._embedded.compraList[0].idCompra", is(1)))
                    .andExpect(jsonPath("$._embedded.compraList[1].idCompra", is(2)));

            verify(compraService).getAllCompras();

            String successMessage = " EXITO: testGetAllCompras - Se obtuvieron " + comprasList.size()
                    + " compras correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllCompras - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetAllComprasListaVacia() throws Exception {
        System.out.println("=== INICIANDO TEST: testGetAllComprasListaVacia ===");
        System.out.flush();
        try {
            // Given
            when(compraService.getAllCompras()).thenReturn(Arrays.asList());

            // When & Then
            mockMvc.perform(get("/api/compras"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"));

            verify(compraService).getAllCompras();

            String successMessage = " EXITO: testGetAllComprasListaVacia - Lista vacía validada correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllComprasListaVacia - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetCompraByIdExistente() throws Exception {
        System.out.println("=== INICIANDO TEST: testGetCompraByIdExistente ===");
        System.out.flush();
        try {
            // Given
            int idCompra = 1;
            when(compraService.getCompraById(idCompra)).thenReturn(compra1);

            // When & Then
            mockMvc.perform(get("/api/compras/{idCompra}", idCompra))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.idCompra", is(1)));

            verify(compraService).getCompraById(idCompra);

            String successMessage = " EXITO: testGetCompraByIdExistente - Compra encontrada con ID " + idCompra + " ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetCompraByIdExistente - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetCompraByIdNoExistente() throws Exception {
        System.out.println("=== INICIANDO TEST: testGetCompraByIdNoExistente ===");
        System.out.flush();
        try {
            // Given
            int idCompra = 999;
            when(compraService.getCompraById(idCompra))
                    .thenThrow(new ResourceNotFoundException("Compra", "ID", idCompra));

            // When & Then
            mockMvc.perform(get("/api/compras/{idCompra}", idCompra))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", is("Compra no encontrado con ID: " + idCompra)))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.path", is("/api/compras/" + idCompra)));

            verify(compraService).getCompraById(idCompra);

            String successMessage = " EXITO: testGetCompraByIdNoExistente - Excepción manejada correctamente para ID "
                    + idCompra + " ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetCompraByIdNoExistente - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetComprasByFecha() throws Exception {
        System.out.println("=== INICIANDO TEST: testGetComprasByFecha ===");
        System.out.flush();
        try {
            // Given
            String fechaInicio = "2023-01-01";
            String fechaFin = "2023-12-31";
            when(compraService.getComprasByFecha(fechaInicio, fechaFin)).thenReturn(comprasList);

            // When & Then
            mockMvc.perform(get("/api/compras/rango/{fechaInicio}/{fechaFin}", fechaInicio, fechaFin))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$._embedded.compraList", hasSize(2)));

            verify(compraService).getComprasByFecha(fechaInicio, fechaFin);

            String successMessage = " EXITO: testGetComprasByFecha - Compras por fecha obtenidas correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetComprasByFecha - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetComprasByFechaInvalida() throws Exception {
        System.out.println("=== INICIANDO TEST: testGetComprasByFechaInvalida ===");
        System.out.flush();
        try {
            // Given
            String fechaInicio = "fecha-invalida";
            String fechaFin = "otra-fecha-invalida";
            when(compraService.getComprasByFecha(fechaInicio, fechaFin))
                    .thenThrow(new BadRequestException("Formato de fecha inválido"));

            // When & Then
            mockMvc.perform(get("/api/compras/rango/{fechaInicio}/{fechaFin}", fechaInicio, fechaFin))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", is("Formato de fecha inválido")))
                    .andExpect(jsonPath("$.timestamp").exists());

            verify(compraService).getComprasByFecha(fechaInicio, fechaFin);

            String successMessage = " EXITO: testGetComprasByFechaInvalida - Excepción BadRequest manejada correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetComprasByFechaInvalida - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testCreateCompra() throws Exception {
        System.out.println("=== INICIANDO TEST: testCreateCompra ===");
        System.out.flush();
        try {
            // Given
            Compra nuevaCompra = new Compra();
            nuevaCompra.setIdCompra(3);
            nuevaCompra.setFechaCompra(new Date());
            nuevaCompra.setNumeroFactura("001003");
            nuevaCompra.setCliente(cliente);
            nuevaCompra.setSucursal(sucursal);

            when(compraService.createCompra(any(Compra.class))).thenReturn(nuevaCompra);

            // When & Then
            mockMvc.perform(post("/api/compras")
                    .contentType("application/hal+json")
                    .content(objectMapper.writeValueAsString(nuevaCompra)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.idCompra", is(3)))
                    .andExpect(jsonPath("$.numeroFactura", is("001003")));

            verify(compraService).createCompra(any(Compra.class));

            String successMessage = " EXITO: testCreateCompra - Compra creada correctamente con ID "
                    + nuevaCompra.getIdCompra() + " ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testCreateCompra - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testUpdateCompraExistente() throws Exception {
        System.out.println("=== INICIANDO TEST: testUpdateCompraExistente ===");
        System.out.flush();
        try {
            // Given
            int idCompra = 1;
            Compra compraActualizada = new Compra();
            compraActualizada.setIdCompra(idCompra);
            compraActualizada.setFechaCompra(new Date());
            compraActualizada.setNumeroFactura("001001-UPD");
            compraActualizada.setCliente(cliente);
            compraActualizada.setSucursal(sucursal);

            when(compraService.updateCompra(eq(idCompra), any(Compra.class))).thenReturn(compraActualizada);

            // When & Then
            mockMvc.perform(put("/api/compras/{idCompra}", idCompra)
                    .contentType("application/hal+json")
                    .content(objectMapper.writeValueAsString(compraActualizada)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.idCompra", is(1)))
                    .andExpect(jsonPath("$.numeroFactura", is("001001-UPD")));

            verify(compraService).updateCompra(eq(idCompra), any(Compra.class));

            String successMessage = " EXITO: testUpdateCompraExistente - Compra actualizada correctamente con ID "
                    + idCompra + " ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testUpdateCompraExistente - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testUpdateCompraNoExistente() throws Exception {
        System.out.println("=== INICIANDO TEST: testUpdateCompraNoExistente ===");
        System.out.flush();
        try {
            // Given
            int idCompra = 999;
            Compra compra = new Compra();
            compra.setIdCompra(idCompra);
            compra.setNumeroFactura("001999");

            when(compraService.updateCompra(eq(idCompra), any(Compra.class)))
                    .thenThrow(new ResourceNotFoundException("Compra", "ID", idCompra));

            // When & Then
            mockMvc.perform(put("/api/compras/{idCompra}", idCompra)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(compra)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", is("Compra no encontrado con ID: " + idCompra)))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.path", is("/api/compras/" + idCompra)));

            verify(compraService).updateCompra(eq(idCompra), any(Compra.class));

            String successMessage = " EXITO: testUpdateCompraNoExistente - Excepción manejada correctamente para ID "
                    + idCompra + " ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testUpdateCompraNoExistente - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testDeleteCompra() throws Exception {
        System.out.println("=== INICIANDO TEST: testDeleteCompra ===");
        System.out.flush();
        try {
            // Given
            int idCompra = 1;
            doNothing().when(compraService).deleteCompra(idCompra);

            // When & Then
            mockMvc.perform(delete("/api/compras/{idCompra}", idCompra))
                    .andExpect(status().isNoContent());

            verify(compraService).deleteCompra(idCompra);

            String successMessage = " EXITO: testDeleteCompra - Compra eliminada correctamente con ID " + idCompra
                    + " ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testDeleteCompra - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testCreateCompraConDiferentesTotales() throws Exception {
        System.out.println("=== INICIANDO TEST: testCreateCompraConDiferentesTotales ===");
        System.out.flush();
        try {
            // Given
            Compra compraMinima = new Compra();
            compraMinima.setIdCompra(4);
            compraMinima.setNumeroFactura("001004");

            when(compraService.createCompra(any(Compra.class))).thenReturn(compraMinima);

            // When & Then
            mockMvc.perform(post("/api/compras")
                    .contentType("application/hal+json")
                    .content(objectMapper.writeValueAsString(compraMinima)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.numeroFactura", is("001004")));

            verify(compraService).createCompra(any(Compra.class));

            String successMessage = " EXITO: testCreateCompraConDiferentesTotales - Compra mínima creada correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testCreateCompraConDiferentesTotales - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }
}
