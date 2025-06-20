package com.ecomarket.ecomarket;

import com.ecomarket.ecomarket.model.*;
import com.ecomarket.ecomarket.repository.*;
import com.ecomarket.ecomarket.util.utils;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private DetalleRepository detalleRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ComunaRepository comunaRepository;
    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Generar Regiones
        Region region1 = new Region();
        region1.setIdRegion(1);
        region1.setNombreRegion("Región Metropolitana");
        regionRepository.save(region1);

        Region region2 = new Region();
        region2.setIdRegion(2);
        region2.setNombreRegion("Valparaíso");
        regionRepository.save(region2);

        // Generar Comunas
        Comuna comuna1 = new Comuna();
        comuna1.setIdComuna(1);
        comuna1.setNombreComuna("Santiago");
        comuna1.setRegion(region1);
        comunaRepository.save(comuna1);

        Comuna comuna2 = new Comuna();
        comuna2.setIdComuna(2);
        comuna2.setNombreComuna("Valparaí");
        comuna2.setRegion(region2);
        comunaRepository.save(comuna2);

        // Generar Sucursales
        Sucursal sucursal1 = new Sucursal();
        sucursal1.setIdSucursal(1);
        sucursal1.setDireccionSucursal("Av. Providencia 123");
        sucursal1.setComuna(comuna1);
        sucursalRepository.save(sucursal1);

        Sucursal sucursal2 = new Sucursal();
        sucursal2.setIdSucursal(2);
        sucursal2.setDireccionSucursal("Av. Libertad 456");
        sucursal2.setComuna(comuna2);
        sucursalRepository.save(sucursal2);

        // Generar Productos
        for (int i = 0; i < 10; i++) {
            Producto producto = new Producto();
            producto.setNombreProducto(faker.commerce().productName());
            producto.setStock(faker.number().numberBetween(0, 1000));
            productoRepository.save(producto);
        }

        // Generar Clientes
        for (int i = 0; i < 50; i++) {
            Cliente cliente = new Cliente();
            int rutnumerico = random.nextInt(9000000) + 1000000;
            cliente.setRun(String.valueOf(rutnumerico));
            cliente.setDv(utils.calcularDv(rutnumerico));
            cliente.setNombres(faker.name().firstName());
            cliente.setApellidos(faker.name().lastName());
            clienteRepository.save(cliente);
        }

        // Generar compras
        List<Cliente> clientes = clienteRepository.findAll();
        List<Sucursal> sucursales = sucursalRepository.findAll();
        for (int i = 0; i < 10; i++) {
            Compra compra = new Compra();
            compra.setIdCompra(i + 1);
            compra.setFechaCompra(new java.util.Date());
            compra.setCliente(clientes.get(random.nextInt(clientes.size())));
            compra.setNumeroFactura(String.valueOf(random.nextInt(999999999)));
            compra.setSucursal(sucursales.get(random.nextInt(sucursales.size())));
            compraRepository.save(compra);
        }

        // Generar Detalles
        List<Producto> productos = productoRepository.findAll();
        List<Compra> compras = compraRepository.findAll();
        for (int i = 0; i < 20; i++) {
            Detalle detalle = new Detalle();
            detalle.setCantidad(faker.number().numberBetween(1, 100));
            detalle.setPrecioUnitario(faker.number().numberBetween(1000, 100000));
            List<String> metodosPago = Arrays.asList("Efectivo", "Tarjeta", "Transferencia");
            detalle.setMetodoPago(metodosPago.get(random.nextInt(metodosPago.size())));
            detalle.setProducto(productos.get(random.nextInt(productos.size())));
            detalle.setCompra(compras.get(random.nextInt(compras.size())));
            detalleRepository.save(detalle);
        }
    }
}
