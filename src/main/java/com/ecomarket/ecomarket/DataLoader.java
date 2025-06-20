package com.ecomarket.ecomarket;

import com.ecomarket.ecomarket.controller.ProductoController;
import com.ecomarket.ecomarket.model.*;
import com.ecomarket.ecomarket.repository.*;
import com.ecomarket.ecomarket.util.utils;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoController productoController;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private DetalleRepository detalleRepository;
    @Autowired
    private ProductoRepository productoRepository;

    DataLoader(ProductoController productoController) {
        this.productoController = productoController;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Generar Productos
        for (int i = 0; i < 10; i++) {
            Producto producto = new Producto();
            producto.setNombreProducto(faker.commerce().productName());
            producto.setStock(faker.number().numberBetween(0, 10000));
            productoRepository.save(producto);
        }

        // Generar Clientes
        for (int i = 0; i < 50; i++) {
            Cliente cliente = new Cliente();
            int rutnumerico = new Random().nextInt(9000000) + 1000000;
            cliente.setRun(String.valueOf(rutnumerico));
            cliente.setDv(utils.calcularDv(rutnumerico));
            cliente.setNombres(faker.name().firstName());
            cliente.setApellidos(faker.name().lastName());
            clienteRepository.save(cliente);
        }
        // Generar compras
        List<Cliente> clientes = clienteRepository.findAll();
        for (int i = 0; i < 10; i++) {
            Compra compra = new Compra();
            compra.setFechaCompra(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS));
            compra.setCliente(clientes.get(random.nextInt(clientes.size())));
            compra.setNumeroFactura(String.valueOf(random.nextInt(999999999)));
            compraRepository.save(compra);
        }
        // Generar Detalles

        List<Producto> productos = productoRepository.findAll();
        List<Compra> compras = compraRepository.findAll();
        for (int i = 0; i < 20; i++) {
            Detalle detalle = new Detalle();
            detalle.setCantidad(faker.number().numberBetween(1, 100));
            double precioUnitariodouble = faker.number().randomDouble(2, 1000, 10000);
            BigDecimal preciouni = BigDecimal.valueOf(precioUnitariodouble);
            detalle.setPrecioUnitario(preciouni);
            List<String> metodosPago = Arrays.asList("Tarjeta de crédito", "Tarjeta de débito",
                    "Transferencia bancaria", "Pago en efectivo", "WebPay", "PayPal", "Crédito en tienda");
            detalle.setMetodoPago(metodosPago.get(random.nextInt(metodosPago.size())));
            detalle.setProducto(productos.get(random.nextInt(productos.size())));
            ;
            detalle.setCompra(compras.get(random.nextInt(compras.size())));
            ;
            detalleRepository.save(detalle);
        }

        //Generar Regiones
        List<Region> regiones = Arrays.asList(
                new Region(2,"Antofagasta"),
                new Region(13,"Metropolitana de Santiago"),
                new Region(14,"Los Ríos")
        );
        for (Region region : regiones) {
            // regionRepository.save(region);
        }

        // Generar Comunas
        
        List<Comuna> comunas = Arrays.asList(
            // Región de Antofagasta (ID 2)
            new Comuna(1, "Antofagasta", regiones.get(0)),
            new Comuna(2, "Mejillones", regiones.get(0)),
            new Comuna(3, "Sierra Gorda", regiones.get(0)),
            new Comuna(4, "Taltal", regiones.get(0)),
            // Región Metropolitana de Santiago (ID 13)
            new Comuna(5, "Santiago", regiones.get(1)),
            new Comuna(6, "Cerrillos", regiones.get(1)),
            new Comuna(7, "Cerro Navia", regiones.get(1)),
            new Comuna(8, "Conchalí", regiones.get(1)),
            new Comuna(9, "El Bosque", regiones.get(1)),
            new Comuna(10, "Estación Central", regiones.get(1)),
            new Comuna(11, "Huechuraba", regiones.get(1)),
            new Comuna(12, "Independencia", regiones.get(1)),
            new Comuna(13, "La Cisterna", regiones.get(1)),
            new Comuna(14, "La Florida", regiones.get(1)),
            new Comuna(15, "La Granja", regiones.get(1)),
            new Comuna(16, "La Pintana", regiones.get(1)),
            new Comuna(17, "La Reina", regiones.get(1)),
            new Comuna(18, "Las Condes", regiones.get(1)),
            new Comuna(19, "Lo Barnechea", regiones.get(1)),
            new Comuna(20, "Lo Espejo", regiones.get(1)),
            new Comuna(21, "Lo Prado", regiones.get(1)),
            new Comuna(22, "Macul", regiones.get(1)),
            new Comuna(23, "Maipú", regiones.get(1)),
            new Comuna(24, "Ñuñoa", regiones.get(1)),
            new Comuna(25, "Pedro Aguirre Cerda", regiones.get(1)),
            new Comuna(26, "Peñalolén", regiones.get(1)),
            new Comuna(27, "Providencia", regiones.get(1)),
            new Comuna(28, "Pudahuel", regiones.get(1)),
            new Comuna(29, "Quilicura", regiones.get(1)),
            new Comuna(30, "Quinta Normal", regiones.get(1)),
            new Comuna(31, "Recoleta", regiones.get(1)),
            new Comuna(32, "Renca", regiones.get(1)),
            new Comuna(33, "San Joaquín", regiones.get(1)),
            new Comuna(34, "San Miguel", regiones.get(1)),
            new Comuna(35, "San Ramón", regiones.get(1)),
            new Comuna(36, "Vitacura", regiones.get(1)),
            // Región de Los Ríos (ID 14)
            new Comuna(37, "Valdivia", regiones.get(2)),
            new Comuna(38, "Corral", regiones.get(2)),
            new Comuna(39, "Lanco", regiones.get(2)),
            new Comuna(40, "Los Lagos", regiones.get(2)),
            new Comuna(41, "Máfil", regiones.get(2)),
            new Comuna(42, "Mariquina", regiones.get(2)),
            new Comuna(43, "Paillaco", regiones.get(2)),
            new Comuna(44, "Panguipulli", regiones.get(2))
        );
    }
}
