package com.duoc.clinica.clinica.controller;


import com.duoc.clinica.clinica.model.Atencion;
import com.duoc.clinica.clinica.service.AtencionService;
import com.duoc.clinica.clinica.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/atenciones")
public class AtencionController {
    @Autowired
    private AtencionService atencionService;
    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<Atencion> crear(@RequestBody Atencion atencion) {
        return ResponseEntity.ok(atencionService.crearAtencion(atencion));
    }

    @GetMapping
    public ResponseEntity<List<Atencion>> listarTodas() {
        return ResponseEntity.ok(atencionService.listarTodas());
    }

    @GetMapping("/medico/{id}")
    public ResponseEntity<List<Atencion>> porMedico(@PathVariable Long id) {
        return ResponseEntity.ok(atencionService.buscarPorMedico(id));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Atencion>> porPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(atencionService.buscarPorPaciente(id));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Atencion>> porFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(atencionService.buscarPorFecha(fecha));
    }

    @GetMapping("/entre-fechas")
    public ResponseEntity<List<Atencion>> entreFechas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(atencionService.buscarEntreFechas(desde, hasta));
    }

    @GetMapping("/costo/menor-a/{monto}")
    public ResponseEntity<List<Atencion>> porCostoMenor(@PathVariable Double monto) {
        return ResponseEntity.ok(atencionService.buscarPorCostoMenorA(monto));
    }

    @GetMapping("/costo/mayor-a/{monto}")
    public ResponseEntity<List<Atencion>> porCostoMayor(@PathVariable Double monto) {
        return ResponseEntity.ok(atencionService.buscarPorCostoMayorA(monto));
    }

    @GetMapping("/estado/{nombre}")
    public ResponseEntity<List<Atencion>> porEstado(@PathVariable String nombre) {
        return ResponseEntity.ok(atencionService.buscarPorEstado(nombre));
    }



}
