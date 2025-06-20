package com.ecomarket.ecomarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "DEPARTAMENTO")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Departamento {
    @Id
    @Column(name = "ID_DEPARTAMENTO")
    private int idDepartamento;

    @Column(name = "NOMBRE_DEPARTAMENTO", nullable = false, length = 50)
    private String nombreDepartamento;

    @Column(name = "CARGO", nullable = false, length = 20)
    private String cargo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REGION_ID_REGION", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPARTAMENTO_ID_DEPARTAMENTO", nullable = false)
    private Empleado empleado;
    


}
