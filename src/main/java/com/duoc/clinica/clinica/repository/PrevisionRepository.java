package com.duoc.clinica.clinica.repository;

import com.duoc.clinica.clinica.model.Prevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrevisionRepository extends JpaRepository<Prevision, Long> {
    Optional<Prevision> findByNombre(String nombre);
}
