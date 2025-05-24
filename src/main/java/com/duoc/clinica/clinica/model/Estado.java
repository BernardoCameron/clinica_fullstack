package com.duoc.clinica.clinica.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String nombre;
    @Column(length = 200, nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "estado")
    private List<Atencion> atenciones;
}
