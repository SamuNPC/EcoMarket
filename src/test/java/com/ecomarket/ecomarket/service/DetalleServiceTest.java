package com.ecomarket.ecomarket.service;

import com.ecomarket.ecomarket.model.Compra;
import com.ecomarket.ecomarket.model.Detalle;
import com.ecomarket.ecomarket.model.Producto;
import com.ecomarket.ecomarket.repository.DetalleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleServiceTest {

    @Mock
    private DetalleRepository detalleRepository;

    @InjectMocks
    private DetalleService detalleService;

    private Detalle detalle1;
    private Detalle detalle2;
    private Compra compra;
    private Producto producto1;
    private Producto producto2;
    private List<Detalle> detallesList;

    @BeforeEach
    void setUp() {
        compra = new Compra();
        compra.setIdCompra(1);

        producto1 = new Producto();
        producto1.setIdProducto(1);
        producto1.setNombreProducto("Producto 1");
        producto1.setStock(10);

        producto2 = new Producto();
        producto2.setIdProducto(2);
        producto2.setNombreProducto("Producto 2");
        producto2.setStock(5);

        detalle1 = new Detalle();
        detalle1.setIdDetalle(1);
        detalle1.setCantidad(3);
        detalle1.setPrecioUnitario(1500);
        detalle1.setMetodoPago("EFECTIVO");
        detalle1.setCompra(compra);
        detalle1.setProducto(producto1);

        detalle2 = new Detalle();
        detalle2.setIdDetalle(2);
        detalle2.setCantidad(2);
        detalle2.setPrecioUnitario(2500);
        detalle2.setMetodoPago("TARJETA");
        detalle2.setCompra(compra);
        detalle2.setProducto(producto2);

        detallesList = Arrays.asList(detalle1, detalle2);
    }

    @Test
    void testGetAllDetalles() {
        System.out.println("=== INICIANDO TEST: testGetAllDetalles ===");
        System.out.flush();
        try {
            // Given
            when(detalleRepository.findAll()).thenReturn(detallesList);

            // When
            List<Detalle> resultado = detalleService.getAllDetalles();

            // Then
            assertNotNull(resultado);
            assertEquals(2, resultado.size());

            Detalle primerDetalle = resultado.get(0);
            assertEquals(1, primerDetalle.getIdDetalle());
            assertEquals(3, primerDetalle.getCantidad());
            assertEquals(1500, primerDetalle.getPrecioUnitario());
            assertEquals("EFECTIVO", primerDetalle.getMetodoPago());

            Detalle segundoDetalle = resultado.get(1);
            assertEquals(2, segundoDetalle.getIdDetalle());
            assertEquals(2, segundoDetalle.getCantidad());
            assertEquals(2500, segundoDetalle.getPrecioUnitario());
            assertEquals("TARJETA", segundoDetalle.getMetodoPago());

            verify(detalleRepository).findAll();

            String successMessage = " EXITO: testGetAllDetalles - Se obtuvieron " + resultado.size()
                    + " detalles correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllDetalles - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetAllDetallesListaVacia() {
        System.out.println("=== INICIANDO TEST: testGetAllDetallesListaVacia ===");
        System.out.flush();
        try {
            // Given
            when(detalleRepository.findAll()).thenReturn(Arrays.asList());

            // When
            List<Detalle> resultado = detalleService.getAllDetalles();

            // Then
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(detalleRepository).findAll();

            String successMessage = " EXITO: testGetAllDetallesListaVacia - Lista vacía validada correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllDetallesListaVacia - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetAllDetallesConRelaciones() {
        System.out.println("=== INICIANDO TEST: testGetAllDetallesConRelaciones ===");
        System.out.flush();
        try {
            // Given
            when(detalleRepository.findAll()).thenReturn(detallesList);

            // When
            List<Detalle> resultado = detalleService.getAllDetalles();

            // Then
            assertNotNull(resultado);
            assertEquals(2, resultado.size());

            // Verificar relación con compra
            assertEquals(1, resultado.get(0).getCompra().getIdCompra());
            assertEquals(1, resultado.get(1).getCompra().getIdCompra());

            // Verificar relación con producto
            assertEquals("Producto 1", resultado.get(0).getProducto().getNombreProducto());
            assertEquals("Producto 2", resultado.get(1).getProducto().getNombreProducto());

            verify(detalleRepository).findAll();

            String successMessage = " EXITO: testGetAllDetallesConRelaciones - Relaciones verificadas correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllDetallesConRelaciones - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testRepositoryInteraction() {
        System.out.println("=== INICIANDO TEST: testRepositoryInteraction ===");
        System.out.flush();
        try {
            // Given
            when(detalleRepository.findAll()).thenReturn(detallesList);

            // When
            detalleService.getAllDetalles();

            // Then
            verify(detalleRepository, times(1)).findAll();
            verifyNoMoreInteractions(detalleRepository);

            String successMessage = " EXITO: testRepositoryInteraction - Interacción con repositorio verificada ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testRepositoryInteraction - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testConstructorDependencyInjection() {
        System.out.println("=== INICIANDO TEST: testConstructorDependencyInjection ===");
        System.out.flush();
        try {
            // Verificar que el servicio se construye correctamente con el repositorio
            // mockeado
            assertNotNull(detalleService);
            assertNotNull(detalleRepository);

            String successMessage = " EXITO: testConstructorDependencyInjection - Inyección de dependencias verificada ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testConstructorDependencyInjection - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetAllDetallesConDiferentesMetodosPago() {
        System.out.println("=== INICIANDO TEST: testGetAllDetallesConDiferentesMetodosPago ===");
        System.out.flush();
        try {
            // Given
            Detalle detalleEfectivo = new Detalle();
            detalleEfectivo.setIdDetalle(3);
            detalleEfectivo.setMetodoPago("EFECTIVO");

            Detalle detalleTarjeta = new Detalle();
            detalleTarjeta.setIdDetalle(4);
            detalleTarjeta.setMetodoPago("TARJETA");

            Detalle detalleTransferencia = new Detalle();
            detalleTransferencia.setIdDetalle(5);
            detalleTransferencia.setMetodoPago("TRANSFERENCIA");

            List<Detalle> detallesVariados = Arrays.asList(detalleEfectivo, detalleTarjeta, detalleTransferencia);
            when(detalleRepository.findAll()).thenReturn(detallesVariados);

            // When
            List<Detalle> resultado = detalleService.getAllDetalles();

            // Then
            assertNotNull(resultado);
            assertEquals(3, resultado.size());
            assertEquals("EFECTIVO", resultado.get(0).getMetodoPago());
            assertEquals("TARJETA", resultado.get(1).getMetodoPago());
            assertEquals("TRANSFERENCIA", resultado.get(2).getMetodoPago());

            String successMessage = " EXITO: testGetAllDetallesConDiferentesMetodosPago - Métodos de pago validados ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllDetallesConDiferentesMetodosPago - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetAllDetallesConValoresMaximos() {
        System.out.println("=== INICIANDO TEST: testGetAllDetallesConValoresMaximos ===");
        System.out.flush();
        try {
            // Given
            Detalle detalleMaximo = new Detalle();
            detalleMaximo.setIdDetalle(999);
            detalleMaximo.setCantidad(999);
            detalleMaximo.setPrecioUnitario(999999);
            detalleMaximo.setMetodoPago("TRANSFERENCIA");

            when(detalleRepository.findAll()).thenReturn(Arrays.asList(detalleMaximo));

            // When
            List<Detalle> resultado = detalleService.getAllDetalles();

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals(999, resultado.get(0).getIdDetalle());
            assertEquals(999, resultado.get(0).getCantidad());
            assertEquals(999999, resultado.get(0).getPrecioUnitario());
            assertTrue(resultado.get(0).getMetodoPago().length() <= 20);

            String successMessage = " EXITO: testGetAllDetallesConValoresMaximos - Valores máximos validados ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllDetallesConValoresMaximos - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }
}
