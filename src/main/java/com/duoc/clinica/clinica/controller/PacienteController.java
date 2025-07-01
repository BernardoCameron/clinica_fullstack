package com.duoc.clinica.clinica.controller;

import com.duoc.clinica.clinica.model.Paciente;
import com.duoc.clinica.clinica.service.AtencionService;
import com.duoc.clinica.clinica.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private AtencionService atencionService;

    /**
     * Crea un nuevo paciente en el sistema.
     *
     * @param paciente objeto con los datos del paciente
     * @return paciente creado y respuesta 201 si fue exitoso
     */

    @Operation(summary = "Crear un nuevo paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "500", description = "Error en el servidor")
    })

    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente) {
        Paciente creado = pacienteService.crearPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Lista todos los pacientes registrados.
     *
     * @return lista de pacientes o 204 si no hay registros
     */

    @Operation(summary = "Listar todos los pacientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida"),
            @ApiResponse(responseCode = "204", description = "No hay pacientes registrados")
    })

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodos();
        return pacientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pacientes);
    }


    /**
     * Busca un paciente segun su RUT.
     *
     * @param run identificador unico del paciente
     * @return paciente encontrado o 404 si no existe
     */

    @Operation(summary = "Buscar paciente por RUN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontro el paciente con el RUN indicado")
    })

    @GetMapping("/buscar/{run}")
    public ResponseEntity<Paciente> buscarPorRun(
            @Parameter(description = "RUN del paciente", required = true, example = "12345678-9")
            @PathVariable String run) {
        Paciente paciente = pacienteService.buscarPorRun(run).orElse(null);

        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(paciente);
    }

    /**
     * Busca pacientes segun su nombre y apellido.
     *
     * @param nombre nombre del paciente
     * @param apellido apellido del paciente
     * @return lista de pacientes o 204 si no hay resultados
     */

    @Operation(summary = "Buscar pacientes por nombre y apellido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Si el paciente existe"),
            @ApiResponse(responseCode = "204", description = "Si no se encontraron pacientes"),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos")
    })

    @GetMapping("/buscar")
    public ResponseEntity<List<Paciente>> buscarPorNombreYApellido(
            @Parameter(description = "Nombre del paciente", required = true)
            @RequestParam String nombre,
            @Parameter(description = "Apellido del paciente", required = true)
            @RequestParam String apellido) {

        List<Paciente> pacientes = pacienteService.buscarPorNombreYApellido(nombre, apellido);

        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pacientes);
    }

    /**
     * Lista pacientes con edad menor al valor indicado.
     *
     * @param edad edad maxima del paciente
     * @return lista de pacientes o 204 si no hay coincidencias
     */

    @Operation(summary = "Listar pacientes con edad menor a un valor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes"),
            @ApiResponse(responseCode = "400", description = "Parametro invalido")
    })

    @GetMapping("/buscar/edad/menor-a/{edad}")
    public ResponseEntity<List<Paciente>> pacientesMenoresDe(
            @Parameter(description = "Edad maxima del paciente", example = "30")
            @PathVariable int edad) {


        List<Paciente> pacientes = pacienteService.pacientesMenoresDe(edad);

        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pacientes);
    }

    /**
     * Lista pacientes con edad mayor al valor indicado.
     *
     * @param edad edad minima del paciente
     * @return lista de pacientes o 204 si no hay coincidencias
     */

    @Operation(summary = "Listar pacientes con edad mayor a la ingresada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes"),
            @ApiResponse(responseCode = "400", description = "Parametro invalido")
    })

    @GetMapping("/buscar/edad/mayor-a/{edad}")
    public ResponseEntity<List<Paciente>> pacientesMayoresDe(
            @Parameter(description = "Edad minima del paciente", example = "60")
            @PathVariable int edad) {

        List<Paciente> pacientes = pacienteService.pacientesMayoresDe(edad);

        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pacientes);
    }

    /**
     * Calcula la deuda total de un paciente segun su ID.
     *
     * @param id identificador del paciente
     * @return deuda calculada o 404 si no se encuentra el paciente
     */

    @Operation(summary = "Calcular deuda total de un paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deuda calculada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro el paciente con el ID indicado"),
            @ApiResponse(responseCode = "400", description = "ID invalido")
    })

    @GetMapping("/deuda/{id}")
    public ResponseEntity<Double> calcularDeuda(
            @Parameter(description = "ID del paciente", required = true, example = "5")
            @PathVariable Long id) {

        Double deuda = pacienteService.calcularDeudaPaciente(id);

        if (deuda == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(deuda);
    }


    /**
     * Lista pacientes que tienen una prevision especifica.
     *
     * @param nombre nombre de la prevision
     * @return lista de pacientes o 204 si no hay resultados
     */

    @Operation(summary = "Listar pacientes por prevision")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados con esa prevision"),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes con esa prevision"),
            @ApiResponse(responseCode = "400", description = "Parametro invalido")
    })

    @GetMapping("/prevision/{nombre}")
    public ResponseEntity<List<Paciente>> porPrevision(
            @Parameter(description = "Nombre de la prevision", required = true, example = "Fonasa")
            @PathVariable String nombre) {

        List<Paciente> pacientes = pacienteService.pacientesPorPrevision(nombre);

        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pacientes);
    }
}
