package com.duoc.clinica.clinica.model;


import jakarta.persistence.*;

@Table(name = "especialidad")
@Entity
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 200, nullable = false)
    private String descripcion;
}
