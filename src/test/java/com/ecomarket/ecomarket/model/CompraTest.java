package com.ecomarket.ecomarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CompraTest {

    private Compra compra;
    private Cliente cliente;
    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        compra = new Compra();
        cliente = new Cliente("12345678", '9', "Juan", "Pérez");
        sucursal = new Sucursal();
        sucursal.setIdSucursal(1);
    }

    @Test
    void testConstructorVacio() {
        assertNotNull(compra);
        assertEquals(0, compra.getIdCompra());
        assertNull(compra.getFechaCompra());
        assertNull(compra.getNumeroFactura());
        assertNull(compra.getCliente());
        assertNull(compra.getSucursal());
        assertNotNull(compra.getDetalles());
        assertTrue(compra.getDetalles().isEmpty());
    }

    @Test
    void testConstructorCompleto() {
        Date fecha = new Date();
        Compra compraCompleta = new Compra(1, fecha, "F001", cliente, sucursal, new ArrayList<>());

        assertEquals(1, compraCompleta.getIdCompra());
        assertEquals(fecha, compraCompleta.getFechaCompra());
        assertEquals("F001", compraCompleta.getNumeroFactura());
        assertEquals(cliente, compraCompleta.getCliente());
        assertEquals(sucursal, compraCompleta.getSucursal());
        assertNotNull(compraCompleta.getDetalles());
    }

    @Test
    void testSettersYGetters() {
        Date fecha = new Date();

        compra.setIdCompra(100);
        compra.setFechaCompra(fecha);
        compra.setNumeroFactura("F002");
        compra.setCliente(cliente);
        compra.setSucursal(sucursal);

        assertEquals(100, compra.getIdCompra());
        assertEquals(fecha, compra.getFechaCompra());
        assertEquals("F002", compra.getNumeroFactura());
        assertEquals(cliente, compra.getCliente());
        assertEquals(sucursal, compra.getSucursal());
    }

    @Test
    void testValidacionNumeroFactura() {
        String numeroFactura = "FACT-2023-001";
        compra.setNumeroFactura(numeroFactura);
        assertEquals(numeroFactura, compra.getNumeroFactura());
        assertTrue(compra.getNumeroFactura().length() <= 50);
    }

    @Test
    void testRelacionConCliente() {
        compra.setCliente(cliente);
        assertNotNull(compra.getCliente());
        assertEquals("12345678", compra.getCliente().getRun());
        assertEquals("Juan", compra.getCliente().getNombres());
    }

    @Test
    void testRelacionConSucursal() {
        compra.setSucursal(sucursal);
        assertNotNull(compra.getSucursal());
        assertEquals(1, compra.getSucursal().getIdSucursal());
    }

    @Test
    void testListaDetalles() {
        Detalle detalle1 = new Detalle();
        detalle1.setIdDetalle(1);

        Detalle detalle2 = new Detalle();
        detalle2.setIdDetalle(2);

        compra.getDetalles().add(detalle1);
        compra.getDetalles().add(detalle2);

        assertEquals(2, compra.getDetalles().size());
        assertTrue(compra.getDetalles().contains(detalle1));
        assertTrue(compra.getDetalles().contains(detalle2));
    }

    @Test
    void testFechaCompra() {
        Date fechaActual = new Date();
        compra.setFechaCompra(fechaActual);

        assertEquals(fechaActual, compra.getFechaCompra());
        assertNotNull(compra.getFechaCompra());
    }

    @Test
    void testEqualsYHashCode() {
        Date fecha = new Date();

        Compra compra1 = new Compra(1, fecha, "F001", cliente, sucursal, new ArrayList<>());
        Compra compra2 = new Compra(1, fecha, "F001", cliente, sucursal, new ArrayList<>());
        Compra compra3 = new Compra(2, fecha, "F002", cliente, sucursal, new ArrayList<>());

        assertEquals(compra1, compra2);
        assertNotEquals(compra1, compra3);
        assertEquals(compra1.hashCode(), compra2.hashCode());
    }

    @Test
    void testToString() {
        Date fecha = new Date();
        compra.setIdCompra(1);
        compra.setFechaCompra(fecha);
        compra.setNumeroFactura("F001");
        compra.setCliente(cliente);

        String toString = compra.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("F001"));
    }
}
