package com.duoc.clinica.clinica.service;

import com.duoc.clinica.clinica.model.Atencion;
import com.duoc.clinica.clinica.model.Especialidad;
import com.duoc.clinica.clinica.model.Medico;
import com.duoc.clinica.clinica.repository.AtencionRepository;
import com.duoc.clinica.clinica.repository.EspecialidadRepository;
import com.duoc.clinica.clinica.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private AtencionRepository atencionRepository;
    @Autowired
    private EspecialidadRepository especialidadRepository;


    public Medico crearMedico(Medico medico) {
        if (medico.getEspecialidad() == null) {
            Especialidad defecto = especialidadRepository.findByNombre("MEDICINA GENERAL")
                    .orElseThrow(() -> new RuntimeException("Especialidad MEDICINA GENERAL no existe"));
            medico.setEspecialidad(defecto);
        }
        return medicoRepository.save(medico);
    }

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> buscarPorRun(String run) {
        return medicoRepository.findByRun(run);
    }

    public List<Medico> buscarPorNombreYApellido(String nombre, String apellido) {
        return medicoRepository.findByNombreAndApellido(nombre, apellido);
    }

    public List<Medico> conAntiguedadMayorA(int anios) {
        LocalDate fechaLimite = LocalDate.now().minusYears(anios);
        return medicoRepository.findByFechaIngresoBefore(fechaLimite);
    }

    public List<Medico> conAntiguedadMenorA(int anios) {
        LocalDate fechaLimite = LocalDate.now().minusYears(anios);
        return medicoRepository.findByFechaIngresoAfter(fechaLimite);
    }

    public Double calcularSueldoTotal(Long idMedico) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        List<Atencion> atenciones = atencionRepository.findMedicoById(idMedico);

        double totalAtenciones = atenciones.stream()
                .mapToDouble(a -> a.getCosto())
                .sum();

        double extra = totalAtenciones * 0.20;

        return medico.getSueldoBase() + extra;
    }
}
