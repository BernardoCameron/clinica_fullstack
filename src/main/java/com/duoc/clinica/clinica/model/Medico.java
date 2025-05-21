package com.duoc.clinica.clinica.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "medico")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Integer id;
    @Column(length = 12, nullable = false)
    private String run;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellido;

    @Column(name = "fecha_contrato", nullable = false)
    private LocalDate fechaContrato;

    @Column(name = "sueldo_base", nullable = false)
    private Integer sueldoBase;

    @Column(length = 100, nullable = false)
    private String correo;

    @Column(length = 20, nullable = false)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

}
