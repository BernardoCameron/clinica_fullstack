package com.duoc.clinica.clinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="atencion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atencion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atencion")
    private Long id;
    @Column(name = "fecha_atencion", nullable = false)
    private LocalDate fechaAtencion;
    @Column(name = "hora_atencion", nullable = false)
    private LocalTime horaAtencion;
    @Column(nullable = false)
    private Double costo;
    @Column(length = 300, nullable = false)
    private String comentario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
}
