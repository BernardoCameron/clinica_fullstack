package com.duoc.clinica.clinica.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "medico")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "El RUN es obligatorio")
    @Column(nullable = false, unique = true)
    private String run;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es válido")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(nullable = false, unique = true)
    private String telefono;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(nullable = false)
    private LocalDate fechaIngreso;

    @NotNull(message = "El sueldo base es obligatorio")
    @Positive(message = "El sueldo debe ser mayor que 0")
    @Column(nullable = false)
    private Double sueldoBase;

    @NotNull(message = "Debe asignar una especialidad")
    @ManyToOne(optional = false)
    @JoinColumn(name = "especialidad_id", nullable = false)
    @JsonIgnore
    private Especialidad especialidad;

    @OneToMany(mappedBy = "medico")
    @JsonIgnore
    private List<Atencion> atenciones;
}
