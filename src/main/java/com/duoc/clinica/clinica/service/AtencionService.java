package com.duoc.clinica.clinica.service;

import com.duoc.clinica.clinica.model.Atencion;
import com.duoc.clinica.clinica.model.Estado;
import com.duoc.clinica.clinica.model.Medico;
import com.duoc.clinica.clinica.model.Paciente;
import com.duoc.clinica.clinica.repository.AtencionRepository;
import com.duoc.clinica.clinica.repository.EstadoRepository;
import com.duoc.clinica.clinica.repository.MedicoRepository;
import com.duoc.clinica.clinica.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AtencionService {
    @Autowired
    private AtencionRepository atencionRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    public Atencion crearAtencion(Atencion atencion) {
        if (atencion.getEstado() == null) {
            Estado pendiente = estadoRepository.findByNombre("Pendiente")
                    .orElseThrow(() -> new RuntimeException("Estado 'Pendiente' no encontrado"));
            atencion.setEstado(pendiente);
        }
        return atencionRepository.save(atencion);
    }

    public List<Atencion> listarTodas() {
        return atencionRepository.findAll();
    }

    public List<Atencion> buscarPorMedico(Long idMedico) {
        return atencionRepository.findByMedico_Id(idMedico);
    }

    public List<Atencion> buscarPorPaciente(Long idPaciente) {
        return atencionRepository.findByPaciente_Id(idPaciente);
    }

    public List<Atencion> buscarPorFecha(LocalDate fecha) {
        return atencionRepository.findByFechaAtencion(fecha);
    }

    public List<Atencion> buscarEntreFechas(LocalDate desde, LocalDate hasta) {
        return atencionRepository.findByFechaAtencionBetween(desde, hasta);
    }

    public List<Atencion> buscarPorCostoMenorA(Double monto) {
        return atencionRepository.findByCostoLessThan(monto);
    }

    public List<Atencion> buscarPorCostoMayorA(Double monto) {
        return atencionRepository.findByCostoGreaterThan(monto);
    }

    public List<Atencion> buscarPorEstado(String nombreEstado) {
        return atencionRepository.findByEstado_NombreIgnoreCase(nombreEstado);
    }





}
