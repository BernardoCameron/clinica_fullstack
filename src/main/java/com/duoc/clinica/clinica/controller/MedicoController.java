package com.duoc.clinica.clinica.controller;


import com.duoc.clinica.clinica.model.Medico;
import com.duoc.clinica.clinica.service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicos")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;

    /**
     * Crea un nuevo médico en el sistema.
     *
     * @param medico Objeto medico a crear.
     * @return Medico creado con código 201.
     */

    @Operation(summary = "Crea un nuevo médico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médico creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos enviados en la solicitud"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PostMapping
    public ResponseEntity<?> crearMedico(@Valid @RequestBody Medico medico, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Error de validación");
        }

        Medico creado = medicoService.crearMedico(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Lista todos los medicos registrados.
     *
     * @return Lista de medicos o 204 si esta vacia.
     */

    @Operation(summary = "Lista todos los médicos registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de médicos obtenida correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay médicos registrados")
    })

    @GetMapping
    public ResponseEntity<List<Medico>> listarMedicos() {
        List<Medico> medicos = medicoService.listarTodos();
        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(medicos);
    }

    /**
     * Busca medicos por nombre y apellido exactos.
     *
     * @param nombre Nombre del medico.
     * @param apellido Apellido del medico.
     * @return Lista de coincidencias o 204 si no hay ninguno.
     */

    @Operation(summary = "Busca médicos por nombre y apellido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médicos encontrados con el nombre y apellido especificados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron médicos con los datos proporcionados"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })

    @GetMapping("/buscar")
    public ResponseEntity<List<Medico>> buscarPorNombreCompleto(
            @Parameter(description = "Nombre del médico", required = true)
            @RequestParam String nombre,
            @Parameter(description = "Apellido del médico", required = true)
            @RequestParam String apellido) {

        if (nombre.isBlank() || apellido.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Medico> resultado = medicoService.buscarPorNombreYApellido(nombre, apellido);

        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }

    /**
     * Lista medicos con antiguedad es menor a los años especificados.
     *
     * @param anios Años de antiguedad maxima.
     * @return Lista de medicos o 204 si no hay ninguno.
     */

    @Operation(summary = "Lista medicos con antiguedad menor a los años especificados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicos con antiguedad menor al valor entregado"),
            @ApiResponse(responseCode = "204", description = "No se encontraron medicos con antiguedad menor al valor entregado"),
            @ApiResponse(responseCode = "400", description = "Parametro inválido")
    })

    @GetMapping("/antiguedad/menor-a/{anios}")
    public ResponseEntity<List<Medico>> conAntiguedadMenorA(
            @Parameter(description = "Cantidad de años de antigüedad máxima", example = "5")
            @PathVariable int anios) {

        if (anios <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Medico> resultado = medicoService.conAntiguedadMenorA(anios);

        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(resultado);
    }

    /**
     * Lista medicos con antiguedad mayor a los años especificados.
     *
     * @param anios Años de antiguedad minima.
     * @return Lista de médicos o 204 si no hay ninguno.
     */

    @Operation(summary = "Lista médicos con antigüedad mayor a los años especificados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médicos con antigüedad mayor al valor entregado"),
            @ApiResponse(responseCode = "204", description = "No se encontraron médicos con antigüedad mayor al valor entregado"),
            @ApiResponse(responseCode = "400", description = "Parámetro inválido")
    })

    @GetMapping("/antiguedad/mayor-a/{anios}")
    public ResponseEntity<List<Medico>> conAntiguedadMayorA(
            @Parameter(description = "Cantidad de años de antigüedad mínima", example = "10")
            @PathVariable int anios) {
        if (anios <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Medico> resultado = medicoService.conAntiguedadMayorA(anios);

        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }

    /**
     * Calcula el sueldo total de un medico segun su ID.
     *
     * @param id ID del medico.
     * @return Sueldo como numero decimal o 404 si no se encuentra nimguno.
     */
    @Operation(summary = "Calcula el sueldo total de un médico segun su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sueldo calculado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró un médico con el ID especificado"),
            @ApiResponse(responseCode = "400", description = "ID invalido")
    })

    @GetMapping("/sueldo/{id}")
    public ResponseEntity<Double> calcularSueldo(
            @Parameter(description = "ID del médico", required = true, example = "1")
            @PathVariable Long id) {

        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Double sueldo = medicoService.calcularSueldoTotal(id);

        if (sueldo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sueldo);
    }



}
