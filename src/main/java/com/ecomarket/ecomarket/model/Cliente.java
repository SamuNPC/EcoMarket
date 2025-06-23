package com.ecomarket.ecomarket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CLIENTE")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Cliente extends RepresentationModel<Cliente> {

    @Id
    @Column(name = "RUN", nullable = false, length = 12)
    private String run;

    @Column(name = "DV", nullable = false, length = 1)
    private char dv;

    @Column(name = "NOMBRES", nullable = false, length = 30)
    private String nombres;

    @Column(name = "APELLIDOS", nullable = false, length = 30)
    private String apellidos;
}