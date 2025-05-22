package com.duoc.clinica.clinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="prevision")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prevision")
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 200, nullable = false)
    private String cobertura;
}
