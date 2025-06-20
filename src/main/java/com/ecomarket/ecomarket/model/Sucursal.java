package com.ecomarket.ecomarket.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SUCURSAL")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUCURSAL")
    private int idSucursal;

    @Column(name = "DIR_SUCURSAL", nullable = false, length = 50)
    private String DireccionSucursal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMUNA_ID_COMUNA", nullable = false)
    private Comuna comuna;


}
