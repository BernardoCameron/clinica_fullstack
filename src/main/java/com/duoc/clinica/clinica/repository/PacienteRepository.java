package com.duoc.clinica.clinica.repository;

import com.duoc.clinica.clinica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByRun(String run);
    List<Paciente> findByNombreAndApellido(String nombre, String apellido);
    List<Paciente> findByFechaNacimientoAfter(LocalDate fechaLimite);
    List<Paciente> findByFechaNacimientoBefore(LocalDate fechaLimite);
    List<Paciente> findByPrevision_NombreIgnoreCase(String nombre);

}
