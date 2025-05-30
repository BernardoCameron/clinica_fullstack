package com.duoc.clinica.clinica.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="estado")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonIgnore
    private List<Atencion> atenciones;
}
