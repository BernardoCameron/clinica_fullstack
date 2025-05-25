package com.duoc.clinica.clinica.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name= "paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long id;
    @Column(length = 12, nullable = false, unique = true)
    private String run;
    @Column(length = 50, nullable = false)
    private String nombre;
    @Column(length = 50, nullable = false)
    private String apellido;
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    @Column(length = 100, nullable = false, unique = true)
    private String correo;
    @Column(length = 20, nullable = false, unique = true)
    private String telefono;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_prevision", nullable = false)
    private Prevision prevision;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    private List<Atencion> atenciones;
}
