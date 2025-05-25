package com.duoc.clinica.clinica.repository;

import com.duoc.clinica.clinica.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    List<Atencion> findByMedico_Id(Long idMedico);
    List<Atencion> findByPaciente_Id(Long idPaciente);
    List<Atencion> findByFechaAtencion(LocalDate fecha);
    List<Atencion> findByFechaAtencionBetween(LocalDate desde, LocalDate hasta);
    List<Atencion> findByCostoLessThan(Double monto);
    List<Atencion> findByCostoGreaterThan(Double monto);
    List<Atencion> findByEstado_NombreIgnoreCase(String nombre);
}
