package com.ecomarket.ecomarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DetalleTest {

    private Detalle detalle;
    private Compra compra;
    private Producto producto;

    @BeforeEach
    void setUp() {
        detalle = new Detalle();

        compra = new Compra();
        compra.setIdCompra(1);

        producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombreProducto("Producto Test");
    }

    @Test
    void testConstructorVacio() {
        assertNotNull(detalle);
        assertEquals(0, detalle.getIdDetalle());
        assertNull(detalle.getCantidad());
        assertNull(detalle.getPrecioUnitario());
        assertNull(detalle.getMetodoPago());
        assertNull(detalle.getCompra());
        assertNull(detalle.getProducto());
    }

    @Test
    void testConstructorCompleto() {
        Detalle detalleCompleto = new Detalle(1, 5, 1500, "EFECTIVO", compra, producto);

        assertEquals(1, detalleCompleto.getIdDetalle());
        assertEquals(5, detalleCompleto.getCantidad());
        assertEquals(1500, detalleCompleto.getPrecioUnitario());
        assertEquals("EFECTIVO", detalleCompleto.getMetodoPago());
        assertEquals(compra, detalleCompleto.getCompra());
        assertEquals(producto, detalleCompleto.getProducto());
    }

    @Test
    void testSettersYGetters() {
        detalle.setIdDetalle(10);
        detalle.setCantidad(3);
        detalle.setPrecioUnitario(2500);
        detalle.setMetodoPago("TARJETA");
        detalle.setCompra(compra);
        detalle.setProducto(producto);

        assertEquals(10, detalle.getIdDetalle());
        assertEquals(3, detalle.getCantidad());
        assertEquals(2500, detalle.getPrecioUnitario());
        assertEquals("TARJETA", detalle.getMetodoPago());
        assertEquals(compra, detalle.getCompra());
        assertEquals(producto, detalle.getProducto());
    }

    @Test
    void testValidacionCantidad() {
        detalle.setCantidad(5);
        assertTrue(detalle.getCantidad() > 0);
        assertEquals(5, detalle.getCantidad());
    }

    @Test
    void testValidacionPrecioUnitario() {
        detalle.setPrecioUnitario(1000);
        assertTrue(detalle.getPrecioUnitario() > 0);
        assertEquals(1000, detalle.getPrecioUnitario());
    }

    @Test
    void testValidacionMetodoPago() {
        String metodoPago = "EFECTIVO";
        detalle.setMetodoPago(metodoPago);
        assertEquals(metodoPago, detalle.getMetodoPago());
        assertTrue(detalle.getMetodoPago().length() <= 20);
    }

    @Test
    void testMetodosPagoValidos() {
        String[] metodosPago = { "EFECTIVO", "TARJETA", "TRANSFERENCIA", "CHEQUE" };

        for (String metodo : metodosPago) {
            detalle.setMetodoPago(metodo);
            assertEquals(metodo, detalle.getMetodoPago());
            assertTrue(detalle.getMetodoPago().length() <= 20);
        }
    }

    @Test
    void testRelacionConCompra() {
        detalle.setCompra(compra);
        assertNotNull(detalle.getCompra());
        assertEquals(1, detalle.getCompra().getIdCompra());
    }

    @Test
    void testRelacionConProducto() {
        detalle.setProducto(producto);
        assertNotNull(detalle.getProducto());
        assertEquals(1, detalle.getProducto().getIdProducto());
        assertEquals("Producto Test", detalle.getProducto().getNombreProducto());
    }

    @Test
    void testCalculoSubtotal() {
        detalle.setCantidad(3);
        detalle.setPrecioUnitario(1500);

        // Calculamos el subtotal manualmente
        int subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
        assertEquals(4500, subtotal);
    }

    @Test
    void testValoresLimite() {
        // Test con valores mínimos
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(1);
        assertEquals(1, detalle.getCantidad());
        assertEquals(1, detalle.getPrecioUnitario());

        // Test con valores altos
        detalle.setCantidad(999);
        detalle.setPrecioUnitario(999999);
        assertEquals(999, detalle.getCantidad());
        assertEquals(999999, detalle.getPrecioUnitario());
    }

    @Test
    void testEqualsYHashCode() {
        Detalle detalle1 = new Detalle(1, 5, 1500, "EFECTIVO", compra, producto);
        Detalle detalle2 = new Detalle(1, 5, 1500, "EFECTIVO", compra, producto);
        Detalle detalle3 = new Detalle(2, 3, 2000, "TARJETA", compra, producto);

        assertEquals(detalle1, detalle2);
        assertNotEquals(detalle1, detalle3);
        assertEquals(detalle1.hashCode(), detalle2.hashCode());
    }

    @Test
    void testToString() {
        detalle.setIdDetalle(1);
        detalle.setCantidad(5);
        detalle.setPrecioUnitario(1500);
        detalle.setMetodoPago("EFECTIVO");

        String toString = detalle.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("5"));
        assertTrue(toString.contains("1500"));
        assertTrue(toString.contains("EFECTIVO"));
    }
}
