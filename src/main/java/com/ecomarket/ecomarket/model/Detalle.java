package com.ecomarket.ecomarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "DETALLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Detalle extends RepresentationModel<Detalle> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE")
    private int idDetalle;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private Integer precioUnitario;

    @Column(name = "MET_PAGO", nullable = false, length = 20)
    private String metodoPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_COMPRA", nullable = false)
    @JsonIgnoreProperties({ "cliente", "detalles" })
    private Compra compra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;
}