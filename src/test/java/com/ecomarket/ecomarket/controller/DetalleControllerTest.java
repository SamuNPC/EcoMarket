package com.ecomarket.ecomarket.controller;

import com.ecomarket.ecomarket.model.Compra;
import com.ecomarket.ecomarket.model.Detalle;
import com.ecomarket.ecomarket.model.Producto;
import com.ecomarket.ecomarket.repository.DetalleRepository;
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
@WebMvcTest(DetalleController.class)
class DetalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DetalleRepository detalleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Detalle detalle1;
    private Detalle detalle2;
    private Compra compra;
    private Producto producto;
    private List<Detalle> detallesList;

    @BeforeEach
    void setUp() {
        compra = new Compra();
        compra.setIdCompra(1);

        producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombreProducto("Producto Test");
        producto.setStock(10);

        detalle1 = new Detalle();
        detalle1.setIdDetalle(1);
        detalle1.setCantidad(3);
        detalle1.setPrecioUnitario(1500);
        detalle1.setMetodoPago("EFECTIVO");
        detalle1.setCompra(compra);
        detalle1.setProducto(producto);

        detalle2 = new Detalle();
        detalle2.setIdDetalle(2);
        detalle2.setCantidad(2);
        detalle2.setPrecioUnitario(2500);
        detalle2.setMetodoPago("TARJETA");
        detalle2.setCompra(compra);
        detalle2.setProducto(producto);

        detallesList = Arrays.asList(detalle1, detalle2);
    }

    @Test
    void testGetAllDetalles() throws Exception {
        // Given
        when(detalleRepository.findAll()).thenReturn(detallesList);

        // When & Then
        mockMvc.perform(get("/api/detalles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idDetalle", is(1)))
                .andExpect(jsonPath("$[0].cantidad", is(3)))
                .andExpect(jsonPath("$[0].precioUnitario", is(1500)))
                .andExpect(jsonPath("$[0].metodoPago", is("EFECTIVO")))
                .andExpect(jsonPath("$[1].idDetalle", is(2)))
                .andExpect(jsonPath("$[1].cantidad", is(2)))
                .andExpect(jsonPath("$[1].precioUnitario", is(2500)))
                .andExpect(jsonPath("$[1].metodoPago", is("TARJETA")));

        verify(detalleRepository).findAll();
    }

    @Test
    void testGetAllDetallesListaVacia() throws Exception {
        // Given
        when(detalleRepository.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/detalles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(detalleRepository).findAll();
    }

    @Test
    void testGetDetalleByIdExistente() throws Exception {
        // Given
        int idDetalle = 1;
        when(detalleRepository.findById(idDetalle)).thenReturn(Arrays.asList(detalle1));

        // When & Then
        mockMvc.perform(get("/api/detalles/{idDetalle}", idDetalle))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idDetalle", is(1)))
                .andExpect(jsonPath("$.cantidad", is(3)))
                .andExpect(jsonPath("$.precioUnitario", is(1500)))
                .andExpect(jsonPath("$.metodoPago", is("EFECTIVO")));

        verify(detalleRepository).findById(idDetalle);
    }

    @Test
    void testGetDetalleByIdNoExistente() throws Exception {
        // Given
        int idDetalle = 999;
        when(detalleRepository.findById(idDetalle)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/detalles/{idDetalle}", idDetalle))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(detalleRepository).findById(idDetalle);
    }

    @Test
    void testCreateDetalle() throws Exception {
        // Given
        Detalle nuevoDetalle = new Detalle();
        nuevoDetalle.setIdDetalle(3);
        nuevoDetalle.setCantidad(5);
        nuevoDetalle.setPrecioUnitario(3000);
        nuevoDetalle.setMetodoPago("TRANSFERENCIA");
        nuevoDetalle.setCompra(compra);
        nuevoDetalle.setProducto(producto);

        when(detalleRepository.save(any(Detalle.class))).thenReturn(nuevoDetalle);

        // When & Then
        mockMvc.perform(post("/api/detalles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoDetalle)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idDetalle", is(3)))
                .andExpect(jsonPath("$.cantidad", is(5)))
                .andExpect(jsonPath("$.precioUnitario", is(3000)))
                .andExpect(jsonPath("$.metodoPago", is("TRANSFERENCIA")));

        verify(detalleRepository).save(any(Detalle.class));
    }

    @Test
    void testUpdateDetalleExistente() throws Exception {
        // Given
        int idDetalle = 1;
        Detalle detalleActualizado = new Detalle();
        detalleActualizado.setIdDetalle(idDetalle);
        detalleActualizado.setCantidad(10);
        detalleActualizado.setPrecioUnitario(2000);
        detalleActualizado.setMetodoPago("CHEQUE");
        detalleActualizado.setCompra(compra);
        detalleActualizado.setProducto(producto);

        when(detalleRepository.existsById(idDetalle)).thenReturn(true);
        when(detalleRepository.save(any(Detalle.class))).thenReturn(detalleActualizado);

        // When & Then
        mockMvc.perform(put("/api/detalles/{idDetalle}", idDetalle)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detalleActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idDetalle", is(1)))
                .andExpect(jsonPath("$.cantidad", is(10)))
                .andExpect(jsonPath("$.precioUnitario", is(2000)))
                .andExpect(jsonPath("$.metodoPago", is("CHEQUE")));

        verify(detalleRepository).existsById(idDetalle);
        verify(detalleRepository).save(any(Detalle.class));
    }

    @Test
    void testUpdateDetalleNoExistente() throws Exception {
        // Given
        int idDetalle = 999;
        Detalle detalle = new Detalle();
        detalle.setIdDetalle(idDetalle);
        detalle.setCantidad(1);

        when(detalleRepository.existsById(idDetalle)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/detalles/{idDetalle}", idDetalle)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detalle)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(detalleRepository).existsById(idDetalle);
        verify(detalleRepository, never()).save(any(Detalle.class));
    }

    @Test
    void testDeleteDetalle() throws Exception {
        // Given
        int idDetalle = 1;
        doNothing().when(detalleRepository).deleteById(idDetalle);

        // When & Then
        mockMvc.perform(delete("/api/detalles/{idDetalle}", idDetalle))
                .andExpect(status().isOk());

        verify(detalleRepository).deleteById(idDetalle);
    }

    @Test
    void testCreateDetalleConDiferentesMetodosPago() throws Exception {
        // Given
        Detalle detalleEfectivo = new Detalle();
        detalleEfectivo.setIdDetalle(4);
        detalleEfectivo.setCantidad(1);
        detalleEfectivo.setPrecioUnitario(1000);
        detalleEfectivo.setMetodoPago("EFECTIVO");

        when(detalleRepository.save(any(Detalle.class))).thenReturn(detalleEfectivo);

        // When & Then
        mockMvc.perform(post("/api/detalles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detalleEfectivo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metodoPago", is("EFECTIVO")));

        verify(detalleRepository).save(any(Detalle.class));
    }

    @Test
    void testCreateDetalleConValoresMaximos() throws Exception {
        // Given
        Detalle detalleMaximo = new Detalle();
        detalleMaximo.setIdDetalle(999);
        detalleMaximo.setCantidad(999);
        detalleMaximo.setPrecioUnitario(999999);
        detalleMaximo.setMetodoPago("TRANSFERENCIA");

        when(detalleRepository.save(any(Detalle.class))).thenReturn(detalleMaximo);

        // When & Then
        mockMvc.perform(post("/api/detalles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detalleMaximo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDetalle", is(999)))
                .andExpect(jsonPath("$.cantidad", is(999)))
                .andExpect(jsonPath("$.precioUnitario", is(999999)));

        verify(detalleRepository).save(any(Detalle.class));
    }
}
