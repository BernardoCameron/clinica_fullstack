package com.duoc.clinica.clinica.controller;

import com.duoc.clinica.clinica.model.Prevision;
import com.duoc.clinica.clinica.repository.PrevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/previsiones")
public class PrevisionController {
    @Autowired
    private PrevisionRepository previsionRepository;

    @PostMapping
    public ResponseEntity<Prevision> crear(@RequestBody Prevision prevision) {
        return ResponseEntity.ok(previsionRepository.save(prevision));
    }

    @GetMapping
    public ResponseEntity<List<Prevision>> listar() {
        return ResponseEntity.ok(previsionRepository.findAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Prevision> buscarPorNombre(@RequestParam String nombre) {
        Optional<Prevision> result = previsionRepository.findByNombre(nombre);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
