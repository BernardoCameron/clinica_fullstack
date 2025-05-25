package com.duoc.clinica.clinica.repository;

import com.duoc.clinica.clinica.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findAll();
    Optional<Medico> findByRun(String run);
    List<Medico> findByNombreAndApellido(String nombre, String apellido);
    List<Medico> findByFechaIngresoBefore(java.time.LocalDate fecha);
    List<Medico> findByFechaIngresoAfter(java.time.LocalDate fecha);
}
