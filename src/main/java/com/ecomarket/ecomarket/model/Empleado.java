package com.ecomarket.ecomarket.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EMPLEADO")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @Column(name = "RUN_EMPLEADO", nullable = false, length = 12)
    private String run_emp;

    @Column(name = "DV", nullable = false, length = 1)
    private char dv_emp;

    @Column(name = "NOMBRES_EMP", nullable = false, length = 100)
    private String nombres_emp;

    @Column(name = "APELLIDOS_EMP", nullable = false, length = 100)
    private String apellidos_emp;

    @Column(name = "SUELDO_EMP", nullable = false, length = 12)
    private String sueldo_emp;

    @Column(name = "FEC_CONTRATO_EMP", nullable = false)
    private Date fec_contraro_emp;

    @Column(name = "EMAIL_EMP", nullable = false, length = 30)
    private String email_emp;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empleado> empleados = new ArrayList<>();
}