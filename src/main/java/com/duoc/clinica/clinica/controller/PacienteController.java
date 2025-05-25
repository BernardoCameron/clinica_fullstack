package com.duoc.clinica.clinica.controller;

import com.duoc.clinica.clinica.model.Paciente;
import com.duoc.clinica.clinica.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteService.crearPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @GetMapping("/buscar/{run}")
    public ResponseEntity<Paciente> buscarPorRun(@PathVariable String run) {
        return pacienteService.buscarPorRun(run)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Paciente>> buscarPorNombreYApellido(
            @RequestParam String nombre,
            @RequestParam String apellido) {
        return ResponseEntity.ok(pacienteService.buscarPorNombreYApellido(nombre, apellido));
    }

    @GetMapping("/buscar/edad/menor-a/{edad}")
    public ResponseEntity<List<Paciente>> pacientesMenoresDe(@PathVariable int edad) {
        return ResponseEntity.ok(pacienteService.pacientesMenoresDe(edad));
    }

    @GetMapping("/buscar/edad/mayor-a/{edad}")
    public ResponseEntity<List<Paciente>> pacientesMayoresDe(@PathVariable int edad) {
        return ResponseEntity.ok(pacienteService.pacientesMayoresDe(edad));
    }
}
