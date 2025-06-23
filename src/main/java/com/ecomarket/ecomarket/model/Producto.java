package com.ecomarket.ecomarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "PRODUCTO")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Producto extends RepresentationModel<Producto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private int idProducto;

    @Column(name = "STOCK", nullable = false)
    private Integer stock;

    @Column(name = "NOMBRE_PRODUCTO", nullable = false, length = 50)
    private String nombreProducto;
}