package com.ecomarket.ecomarket;
import com.ecomarket.ecomarket.controller.ProductoController;
import com.ecomarket.ecomarket.model.*;
import com.ecomarket.ecomarket.repository.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Profile("dev")
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
    producto.setIdProducto(i+1);
    producto.setNombreProducto(faker.commerce().productName());
    producto.setStock(faker.number().numberBetween(0, 10000));
    double precioDouble = faker.number().randomDouble(2, 2, 10000);
    BigDecimal precio = BigDecimal.valueOf(precioDouble);
    producto.setPrecio(precio);
    productoRepository.save(producto);
    }
    
    // Generar Clientes

    List<String> digitosVerificadores = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "K"); //Mientras no implementamos la validacion del rut//
    for (int i = 0; i < 50; i++) {
    Cliente cliente = new Cliente();
    cliente.setRun(faker.idNumber().valid());
    cliente.setDv(digitosVerificadores.get(random.nextInt(digitosVerificadores.size())));
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
    compra.setNumeroFactura(faker.number().toString());
    compraRepository.save(compra);
    }
     // Generar Detalles

    List<Producto> productos = productoRepository.findAll();
    List<Compra> compras= compraRepository.findAll();
    for (int i = 0; i < 20; i++) {
    Detalle detalle = new Detalle();
    detalle.setIdDetalle(i + 1);
    detalle.setCantidad(faker.number().numberBetween(1,100));
    double precioUnitariodouble = faker.number().randomDouble(2, 1000, 10000);
    BigDecimal preciouni = BigDecimal.valueOf(precioUnitariodouble);
    detalle.setPrecioUnitario(preciouni);
    List<String> metodosPago = Arrays.asList("Tarjeta de crédito", "Tarjeta de débito", "Transferencia bancaria","Pago en efectivo","WebPay","PayPal","Crédito en tienda");
    detalle.setMetodoPago(metodosPago.get(random.nextInt(metodosPago.size())));
    detalle.setProducto(productos.get(random.nextInt(productos.size())));;
    detalle.setCompra(compras.get(random.nextInt(compras.size())));;
    detalleRepository.save(detalle); 
    }
}
}




