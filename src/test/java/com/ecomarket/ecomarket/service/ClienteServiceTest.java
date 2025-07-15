package com.ecomarket.ecomarket.service;

import com.ecomarket.ecomarket.model.Cliente;
import com.ecomarket.ecomarket.repository.ClienteRepository;
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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

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
    void testGetAllClientes() {
        System.out.println("=== INICIANDO TEST: testGetAllClientes ===");
        System.out.flush();
        try {
            // Given
            when(clienteRepository.findAll()).thenReturn(clientesList);

            // When
            List<Cliente> resultado = clienteService.getAllClientes();

            // Then
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("Juan", resultado.get(0).getNombres());
            assertEquals("María", resultado.get(1).getNombres());
            verify(clienteRepository).findAll();

            String successMessage = " EXITO: testGetAllClientes - Se obtuvieron " + resultado.size()
                    + " clientes correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllClientes - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }

    @Test
    void testGetAllClientesListaVacia() {
        System.out.println("=== INICIANDO TEST: testGetAllClientesListaVacia ===");
        System.out.flush();
        try {
            // Given
            when(clienteRepository.findAll()).thenReturn(Arrays.asList());

            // When
            List<Cliente> resultado = clienteService.getAllClientes();

            // Then
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(clienteRepository).findAll();

            String successMessage = " EXITO: testGetAllClientesListaVacia - Lista vacía validada correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllClientesListaVacia - " + e.getMessage() + " ";
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
            when(clienteRepository.findAll()).thenReturn(clientesList);

            // When
            clienteService.getAllClientes();

            // Then
            verify(clienteRepository, times(1)).findAll();
            verifyNoMoreInteractions(clienteRepository);

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
            assertNotNull(clienteService);
            assertNotNull(clienteRepository);

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
    void testGetAllClientesConDatosCompletos() {
        System.out.println("=== INICIANDO TEST: testGetAllClientesConDatosCompletos ===");
        System.out.flush();
        try {
            // Given
            Cliente clienteCompleto = new Cliente("11111111", '1', "Carlos Alberto", "Rodríguez Martínez");
            when(clienteRepository.findAll()).thenReturn(Arrays.asList(clienteCompleto));

            // When
            List<Cliente> resultado = clienteService.getAllClientes();

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            Cliente clienteResultado = resultado.get(0);
            assertEquals("11111111", clienteResultado.getRun());
            assertEquals('1', clienteResultado.getDv());
            assertEquals("Carlos Alberto", clienteResultado.getNombres());
            assertEquals("Rodríguez Martínez", clienteResultado.getApellidos());

            String successMessage = " EXITO: testGetAllClientesConDatosCompletos - Datos completos validados correctamente ";
            System.out.println(successMessage);
            System.out.flush();
        } catch (Exception e) {
            String errorMessage = " ERROR: testGetAllClientesConDatosCompletos - " + e.getMessage() + " ";
            System.err.println(errorMessage);
            System.err.flush();
            throw e;
        }
    }
}
