package com.duoc.clinica.clinica.controller;

import com.duoc.clinica.clinica.model.Estado;
import com.duoc.clinica.clinica.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @PostMapping
    public ResponseEntity<Estado> crear(@RequestBody Estado estado) {
        return ResponseEntity.ok(estadoRepository.save(estado));
    }

    @GetMapping
    public ResponseEntity<List<Estado>> listar() {
        return ResponseEntity.ok(estadoRepository.findAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Estado> buscarPorNombre(@RequestParam String nombre) {
        Optional<Estado> estado = estadoRepository.findByNombre(nombre);
        return estado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
