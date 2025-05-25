package com.duoc.clinica.clinica.controller;


import com.duoc.clinica.clinica.model.Medico;
import com.duoc.clinica.clinica.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicos")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<Medico> crearMedico(@RequestBody Medico medico) {
        return ResponseEntity.ok(medicoService.crearMedico(medico));
    }

    @GetMapping
    public ResponseEntity<List<Medico>> listarMedicos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Medico>> buscarPorNombreCompleto(
            @RequestParam String nombre,
            @RequestParam String apellido) {
        return ResponseEntity.ok(medicoService.buscarPorNombreYApellido(nombre, apellido));
    }

    @GetMapping("/antiguedad/menor-a/{anios}")
    public ResponseEntity<List<Medico>> conAntiguedadMenorA(@PathVariable int anios) {
        return ResponseEntity.ok(medicoService.conAntiguedadMenorA(anios));
    }

    @GetMapping("/antiguedad/mayor-a/{anios}")
    public ResponseEntity<List<Medico>> conAntiguedadMayorA(@PathVariable int anios) {
        return ResponseEntity.ok(medicoService.conAntiguedadMayorA(anios));
    }

    @GetMapping("/{id}/sueldo")
    public ResponseEntity<Double> calcularSueldoTotal(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.calcularSueldoTotal(id));
    }
}
