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
    @Column(name = "ID_SUCURSAL")
    private int idSucursal;

    @Column(name = "DIR_SUCURSAL", length = 100)
    private String direccionSucursal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_COMUNA")
    private Comuna comuna;

}
