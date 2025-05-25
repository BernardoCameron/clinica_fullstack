package com.duoc.clinica.clinica.Config;

import com.duoc.clinica.clinica.model.Especialidad;
import com.duoc.clinica.clinica.model.Estado;
import com.duoc.clinica.clinica.model.Prevision;
import com.duoc.clinica.clinica.repository.EspecialidadRepository;
import com.duoc.clinica.clinica.repository.EstadoRepository;
import com.duoc.clinica.clinica.repository.PrevisionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(PrevisionRepository previsionRepo,
                                      EspecialidadRepository especialidadRepo,
                                      EstadoRepository estadoRepo) {
        return args -> {

            // Previsiones
            if (previsionRepo.findByNombre("FONASA").isEmpty()) {
                previsionRepo.save(new Prevision(null, "FONASA", 0.50, null));
            }

            if (previsionRepo.findByNombre("ISAPRE").isEmpty()) {
                previsionRepo.save(new Prevision(null, "ISAPRE", 0.60, null));
            }

            // Especialidades
            if (especialidadRepo.findByNombre("MEDICINA GENERAL").isEmpty()) {
                especialidadRepo.save(new Especialidad(null, "MEDICINA GENERAL", "Especialidad básica general", null));
            }

            // Estados
            if (estadoRepo.findByNombre("Pendiente").isEmpty()) {
                estadoRepo.save(new Estado(null, "Pendiente", "Estado por defecto al ingresar una atención", null));
            }

            if (estadoRepo.findByNombre("Alta").isEmpty()) {
                estadoRepo.save(new Estado(null, "Alta", "Atención finalizada", null));
            }

            if (estadoRepo.findByNombre("Hospitalizado").isEmpty()) {
                estadoRepo.save(new Estado(null, "Hospitalizado", "Paciente hospitalizado", null));
            }
        };
    }
}
