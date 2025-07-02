package com.duoc.clinica.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "atencion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atencion")
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha_atencion", nullable = false)
    private LocalDate fechaAtencion;

    @NotNull(message = "La hora es obligatoria")
    @Column(name = "hora_atencion", nullable = false)
    private LocalTime horaAtencion;

    @NotNull(message = "El costo es obligatorio")
    @Column(nullable = false)
    private Double costo;

    @NotBlank(message = "El comentario no puede estar vacio")
    @Column(length = 300, nullable = false)
    private String comentario;

    @NotNull(message = "El estado es obligatorio")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @NotNull(message = "El paciente es obligatorio")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @NotNull(message = "El medico es obligatorio")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
}
