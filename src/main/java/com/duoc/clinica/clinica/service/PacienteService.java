package com.duoc.clinica.clinica.service;

import com.duoc.clinica.clinica.model.Paciente;
import com.duoc.clinica.clinica.model.Prevision;
import com.duoc.clinica.clinica.repository.PacienteRepository;
import com.duoc.clinica.clinica.repository.PrevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private PrevisionRepository previsionRepository;

    public Paciente crearPaciente(Paciente paciente) {
        if (paciente.getPrevision() == null) {
            Prevision fonasa = previsionRepository.findByNombre("FONASA")
                    .orElseThrow(() -> new RuntimeException("Previsión FONASA no encontrada"));
            paciente.setPrevision(fonasa);
        }
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscarPorRun(String run) {
        return pacienteRepository.findByRun(run);
    }

    public List<Paciente> buscarPorNombreYApellido(String nombre, String apellido) {
        return pacienteRepository.findByNombreAndApellido(nombre, apellido);
    }

    public List<Paciente> pacientesMenoresDe(int edad) {
        LocalDate fechaLimite = LocalDate.now().minusYears(edad);
        return pacienteRepository.findByFechaNacimientoAfter(fechaLimite);
    }

    public List<Paciente> pacientesMayoresDe(int edad) {
        LocalDate fechaLimite = LocalDate.now().minusYears(edad);
        return pacienteRepository.findByFechaNacimientoBefore(fechaLimite);
    }
}
