package com.ecomarket.ecomarket.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "COMPRA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Compra extends RepresentationModel<Compra> {

    @Id
    @Column(name = "ID_COMPRA")
    private int idCompra;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "FECHA_COMPRA", nullable = false)
    private Date fechaCompra;

    @Column(name = "NRO_FACTURA", nullable = false, length = 50)
    private String numeroFactura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTE_RUN", nullable = false, referencedColumnName = "RUN")
    @JsonIgnoreProperties("compras")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detalle> detalles = new ArrayList<>();
}
