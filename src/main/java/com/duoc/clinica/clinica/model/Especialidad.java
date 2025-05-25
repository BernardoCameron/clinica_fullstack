package com.duoc.clinica.clinica.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "especialidad")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String nombre;

    @Column(length = 200, nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "especialidad")
    @JsonIgnore
    private List<Medico> medicos;
}
