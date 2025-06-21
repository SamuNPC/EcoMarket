package com.ecomarket.ecomarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    @Test
    void testConstructorVacio() {
        assertNotNull(cliente);
        assertNull(cliente.getRun());
        assertEquals('\0', cliente.getDv());
        assertNull(cliente.getNombres());
        assertNull(cliente.getApellidos());
    }

    @Test
    void testConstructorCompleto() {
        Cliente clienteCompleto = new Cliente("12345678", '9', "Juan", "Pérez");

        assertEquals("12345678", clienteCompleto.getRun());
        assertEquals('9', clienteCompleto.getDv());
        assertEquals("Juan", clienteCompleto.getNombres());
        assertEquals("Pérez", clienteCompleto.getApellidos());
    }

    @Test
    void testSettersYGetters() {
        cliente.setRun("87654321");
        cliente.setDv('0');
        cliente.setNombres("María");
        cliente.setApellidos("González");

        assertEquals("87654321", cliente.getRun());
        assertEquals('0', cliente.getDv());
        assertEquals("María", cliente.getNombres());
        assertEquals("González", cliente.getApellidos());
    }

    @Test
    void testValidacionRun() {
        cliente.setRun("12345678");
        assertNotNull(cliente.getRun());
        assertEquals(8, cliente.getRun().length());
    }

    @Test
    void testValidacionDv() {
        cliente.setDv('K');
        assertEquals('K', cliente.getDv());
    }

    @Test
    void testValidacionNombres() {
        String nombres = "Juan Carlos";
        cliente.setNombres(nombres);
        assertEquals(nombres, cliente.getNombres());
        assertTrue(cliente.getNombres().length() <= 30);
    }

    @Test
    void testValidacionApellidos() {
        String apellidos = "García López";
        cliente.setApellidos(apellidos);
        assertEquals(apellidos, cliente.getApellidos());
        assertTrue(cliente.getApellidos().length() <= 30);
    }

    @Test
    void testEqualsYHashCode() {
        Cliente cliente1 = new Cliente("12345678", '9', "Juan", "Pérez");
        Cliente cliente2 = new Cliente("12345678", '9', "Juan", "Pérez");
        Cliente cliente3 = new Cliente("87654321", '0', "María", "González");

        assertEquals(cliente1, cliente2);
        assertNotEquals(cliente1, cliente3);
        assertEquals(cliente1.hashCode(), cliente2.hashCode());
    }

    @Test
    void testToString() {
        cliente.setRun("12345678");
        cliente.setDv('9');
        cliente.setNombres("Juan");
        cliente.setApellidos("Pérez");

        String toString = cliente.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("12345678"));
        assertTrue(toString.contains("Juan"));
        assertTrue(toString.contains("Pérez"));
    }
}
