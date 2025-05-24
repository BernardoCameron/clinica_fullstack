package com.duoc.clinica.clinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="prevision")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prevision")
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false)
    private Double cobertura;

    @OneToMany(mappedBy = "prevision")
    private List<Paciente> pacientes;
}
