package com.duoc.clinica.clinica.controller;

import com.duoc.clinica.clinica.model.Especialidad;
import com.duoc.clinica.clinica.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @PostMapping
    public ResponseEntity<Especialidad> crearEspecialidad(@RequestBody Especialidad especialidad) {
        return ResponseEntity.ok(especialidadRepository.save(especialidad));
    }

    @GetMapping
    public ResponseEntity<List<Especialidad>> listar() {
        return ResponseEntity.ok(especialidadRepository.findAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Especialidad> buscarPorNombre(@RequestParam String nombre) {
        Optional<Especialidad> esp = especialidadRepository.findByNombre(nombre);
        return esp.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
