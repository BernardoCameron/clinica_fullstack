package com.duoc.clinica.clinica.model;

import jakarta.persistence.*;

@Entity
@Table(name="id_estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 200, nullable = false)
    private String descripcion;
}
