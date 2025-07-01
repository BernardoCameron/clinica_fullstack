package com.duoc.clinica.clinica.Config;

import com.duoc.clinica.clinica.model.*;
import com.duoc.clinica.clinica.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EspecialidadRepository especialidadRepository;
    @Autowired
    private PrevisionRepository previsionRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private AtencionRepository atencionRepository;

    @Override
    public void run(String... args) throws Exception {

        // ESTADOS
        Estado estado1 = null, estado2 = null, estado3 = null;

        if (estadoRepository.count() == 0) {
            estado1 = estadoRepository.save(new Estado(null, "PENDIENTE", "Paciente a espera de atencion", null));
            estado2 = estadoRepository.save(new Estado(null, "HOSPITALIZADO", "Paciente hospitalizado y en observacion", null));
            estado3 = estadoRepository.save(new Estado(null, "ALTA", "Paciente en estado libre para irse a casa", null));
        } else {
            estado1 = estadoRepository.findByNombre("PENDIENTE").orElse(null);
            estado2 = estadoRepository.findByNombre("HOSPITALIZADO").orElse(null);
            estado3 = estadoRepository.findByNombre("ALTA").orElse(null);
        }

        //Especialidad

        Especialidad esp1 = null, esp2 = null, esp3 = null, esp4 = null;

        if (especialidadRepository.count() == 0) {
            esp1 = especialidadRepository.save(new Especialidad(null, "Medicina General", "Medicina general para toda la familia.", null));
            esp2 = especialidadRepository.save(new Especialidad(null, "Pediatria", "Atencion de ninos y adolescentes.", null));
            esp3 = especialidadRepository.save(new Especialidad(null, "Cardiologia", "Enfermedades del corazon.", null));
            esp4 = especialidadRepository.save(new Especialidad(null, "Dermatologia", "Enfermedades de la piel.", null));
        } else {
            esp1 = especialidadRepository.findByNombre("Medicina General").orElse(null);
            esp2 = especialidadRepository.findByNombre("Pediatria").orElse(null);
            esp3 = especialidadRepository.findByNombre("Cardiologia").orElse(null);
            esp4 = especialidadRepository.findByNombre("Dermatologia").orElse(null);
        }

        // PREVISIONES
        Prevision prev1 = null, prev2 = null;

        if (previsionRepository.count() == 0) {
            prev1 = previsionRepository.save(new Prevision(null, "FONASA", 50.0, null));
            prev2 = previsionRepository.save(new Prevision(null, "ISAPRE", 60.0, null));
        } else {
            prev1 = previsionRepository.findByNombre("FONASA").orElse(null);
            prev2 = previsionRepository.findByNombre("ISAPRE").orElse(null);
        }
        Medico m1 = null, m2 = null, m3 = null;

        if (medicoRepository.count() == 0) {
            m1 = medicoRepository.save(new Medico(null, "Julio", "Perez", "10418567-1", "ju.perez@hospital.cl", "569845823", LocalDate.parse("2020-03-15"), 900000.0, esp1, null));
            m2 = medicoRepository.save(new Medico(null, "Ana", "Morales", "11345987-2", "ana.morales@hospital.cl", "56987451236", LocalDate.parse("2018-09-01"), 950000.0, esp2, null));
            m3 = medicoRepository.save(new Medico(null, "Carlos", "Rojas", "12456879-5", "carlos.rojas@hospital.cl", "56976548932", LocalDate.parse("2019-06-10"), 910000.0, esp4, null));
        } else {
            m1 = medicoRepository.findByRun("10418567-1").orElse(null);
            m2 = medicoRepository.findByRun("11345987-2").orElse(null);
            m3 = medicoRepository.findByRun("12456879-5").orElse(null);
        }

        // PACIENTES
        Paciente p1 = null, p2 = null, p3 = null, p4 = null, p5 = null;

        if (pacienteRepository.count() == 0) {
            p1 = pacienteRepository.save(new Paciente(null, "15123588-2", "Maria", "Sanchez", LocalDate.parse("1992-05-20"), "ma.sanchez@correo.cl", "586989652", prev1, null));
            p2 = pacienteRepository.save(new Paciente(null, "16111222-3", "Luis", "Gonzalez", LocalDate.parse("1985-11-12"), "lu.gonzalez@correo.cl", "598761234", prev2, null));
            p3 = pacienteRepository.save(new Paciente(null, "17222333-4", "Patricia", "Castro", LocalDate.parse("1990-08-30"), "pa.castro@correo.cl", "591234567", prev1, null));
            p4 = pacienteRepository.save(new Paciente(null, "18333444-5", "Jorge", "Ramirez", LocalDate.parse("1978-03-22"), "jo.ramirez@correo.cl", "587654321", prev1, null));
            p5 = pacienteRepository.save(new Paciente(null, "19444555-6", "Camila", "Torres", LocalDate.parse("2000-01-15"), "ca.torres@correo.cl", "599876543", prev2, null));
        } else {
            p1 = pacienteRepository.findByRun("15123588-2").orElse(null);
            p2 = pacienteRepository.findByRun("16111222-3").orElse(null);
            p3 = pacienteRepository.findByRun("17222333-4").orElse(null);
            p4 = pacienteRepository.findByRun("18333444-5").orElse(null);
            p5 = pacienteRepository.findByRun("19444555-6").orElse(null);
        }

        // ATENCIONES

        if (atencionRepository.count() == 0) {
            Atencion a1 = new Atencion(null, LocalDate.parse("2025-05-20"), LocalTime.parse("10:30:00"), 15000.0, "Dolor de cabeza", estado1, p1, m1);
            Atencion a2 = new Atencion(null, LocalDate.parse("2025-05-20"), LocalTime.parse("11:00:00"), 18000.0, "Control pediatrico", estado3, p2, m2);
            Atencion a3 = new Atencion(null, LocalDate.parse("2025-05-21"), LocalTime.parse("09:45:00"), 20000.0, "Alergia", estado2, p3, m3);
            Atencion a4 = new Atencion(null, LocalDate.parse("2025-05-21"), LocalTime.parse("12:15:00"), 22000.0, "Taquicardia", estado1, p4, m1);
            Atencion a5 = new Atencion(null, LocalDate.parse("2025-05-21"), LocalTime.parse("14:00:00"), 17000.0, "Post operacion", estado3, p5, m2);
            Atencion a6 = new Atencion(null, LocalDate.parse("2025-05-22"), LocalTime.parse("08:30:00"), 19000.0, "Infeccion cutanea", estado2, p1, m3);
            Atencion a7 = new Atencion(null, LocalDate.parse("2025-05-22"), LocalTime.parse("10:00:00"), 21000.0, "Hipertension", estado1, p2, m1);
            Atencion a8 = new Atencion(null, LocalDate.parse("2025-05-23"), LocalTime.parse("11:30:00"), 16000.0, "Fiebre", estado2, p3, m2);
            Atencion a9 = new Atencion(null, LocalDate.parse("2025-05-23"), LocalTime.parse("13:45:00"), 23000.0, "Dolor de pecho", estado3, p4, m1);
            Atencion a10 = new Atencion(null, LocalDate.parse("2025-05-23"), LocalTime.parse("15:00:00"), 17500.0, "Consulta preventiva", estado1, p5, m3);


            atencionRepository.save(a1);
            atencionRepository.save(a2);
            atencionRepository.save(a3);
            atencionRepository.save(a4);
            atencionRepository.save(a5);
            atencionRepository.save(a6);
            atencionRepository.save(a7);
            atencionRepository.save(a8);
            atencionRepository.save(a9);
            atencionRepository.save(a10);
        }
    }
}