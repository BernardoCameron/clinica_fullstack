package com.duoc.clinica.clinica.controller;


import com.duoc.clinica.clinica.model.Atencion;
import com.duoc.clinica.clinica.service.AtencionService;
import com.duoc.clinica.clinica.service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    /**
     * Guarda una nueva atencion en el sistema.
     *
     * @param atencion datos de la atencion
     * @return la atencion guardada con respuesta 201 si fue exitosa
     */

    @Operation(summary = "Crear una nueva atencion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atencion creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del sistema")
    })

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Atencion atencion, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Error en los datos de la atencion");
        }

        Atencion creada = atencionService.crearAtencion(atencion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /**
     * Entrega una lista con todas las atenciones registradas.
     *
     * @return lista de atenciones o 204 si no hay ninguna
     */

    @Operation(summary = "Listar todas las atenciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones obtenida"),
            @ApiResponse(responseCode = "204", description = "No hay atenciones registradas")
    })

    @GetMapping
    public ResponseEntity<List<Atencion>> listarTodas() {
        List<Atencion> lista = atencionService.listarTodas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);

    }

    /**
     * Entrega una lista de atenciones hechas por un medico especifico.
     *
     * @param id identificador del medico
     * @return lista de atenciones o 204 si no hay ninguna
     */

    @Operation(summary = "Buscar atenciones por ID de medico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atenciones encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron atenciones para ese medico"),
            @ApiResponse(responseCode = "400", description = "Parametro invalido")
    })

    @GetMapping("/medico/{id}")
    public ResponseEntity<List<Atencion>> porMedico(
            @Parameter(description = "ID del medico", required = true, example = "1")
            @PathVariable Long id) {

        List<Atencion> lista = atencionService.buscarPorMedico(id);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);

    }

    /**
     * Entrega una lista de atenciones hechas a un paciente especifico.
     *
     * @param id identificador del paciente
     * @return lista de atenciones o 204 si no hay resultados
     */

    @Operation(summary = "Buscar atenciones por ID de paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atenciones encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron atenciones para ese paciente"),
            @ApiResponse(responseCode = "400", description = "Parametro invalido")
    })
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Atencion>> porPaciente(
            @Parameter(description = "ID del paciente", required = true, example = "2")
            @PathVariable Long id) {
        List<Atencion> lista = atencionService.buscarPorPaciente(id);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);

    }

    /**
     * Busca atenciones realizadas en una fecha especifica.
     *
     * @param fecha fecha de la atencion en formato yyyy-mm-dd
     * @return lista de atenciones o 204 si no hay ninguna
     */
    @Operation(summary = "Buscar atenciones por fecha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atenciones encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron atenciones en esa fecha"),
            @ApiResponse(responseCode = "400", description = "Fecha invalida")
    })

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Atencion>> porFecha(
            @Parameter(description = "Fecha de atencion en formato yyyy-MM-dd", example = "2023-10-15")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Atencion> lista = atencionService.buscarPorFecha(fecha);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    /**
     * Lista atenciones realizadas entre dos fechas.
     *
     * @param desde fecha inicial
     * @param hasta fecha final
     * @return lista de atenciones o 204 si no hay resultados
     */

    @Operation(summary = "Buscar atenciones entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atenciones encontradas en el rango"),
            @ApiResponse(responseCode = "204", description = "No se encontraron atenciones en ese rango de fechas"),
            @ApiResponse(responseCode = "400", description = "Fechas invalidas")
    })

    @GetMapping("/entre-fechas")
    public ResponseEntity<List<Atencion>> entreFechas(
            @Parameter(description = "Fecha inicial en formato yyyy-MM-dd", example = "2023-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha final en formato yyyy-MM-dd", example = "2023-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        List<Atencion> lista = atencionService.buscarEntreFechas(desde, hasta);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    /**
     * Lista atenciones que sean menor al monto indicado.
     *
     * @param monto limite superior del costo
     * @return lista de atenciones o 204 si no hay resultados
     */

    @Operation(summary = "Buscar atenciones por costo menor a un valor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atenciones encontradas con costo menor al monto indicado"),
            @ApiResponse(responseCode = "204", description = "No se encontraron atenciones con ese criterio"),
            @ApiResponse(responseCode = "400", description = "Parametro invalido")
    })

    @GetMapping("/costo/menor-a/{monto}")
    public ResponseEntity<List<Atencion>> porCostoMenor(
            @Parameter(description = "Monto maximo de costo", example = "50000")
            @PathVariable Double monto) {

        List<Atencion> lista = atencionService.buscarPorCostoMenorA(monto);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    /**
     * Lista atenciones mayores al monto indicado.
     *
     * @param monto limite del costo
     * @return lista de atenciones o 204 si no hay resultados
     */

    @Operation(summary = "Buscar atenciones por costo mayor a un valor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atenciones encontradas con costo mayor al monto indicado"),
            @ApiResponse(responseCode = "204", description = "No se encontraron atenciones con ese criterio"),
            @ApiResponse(responseCode = "400", description = "Parametro invalido")
    })

    @GetMapping("/costo/mayor-a/{monto}")
    public ResponseEntity<List<Atencion>> porCostoMayor(
            @Parameter(description = "Monto minimo de costo", example = "100000")
            @PathVariable Double monto) {

        List<Atencion> lista = atencionService.buscarPorCostoMayorA(monto);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);

    }

    /**
     * Lista atenciones segun um estado especifico.
     *
     * @param nombre nombre del estado (ej: realizada, pendiente)
     * @return lista de atenciones o 204 si no hay resultados
     */
    @Operation(summary = "Buscar atenciones por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atenciones encontradas con ese estado"),
            @ApiResponse(responseCode = "204", description = "No se encontraron atenciones con ese estado"),
            @ApiResponse(responseCode = "400", description = "Parametro invalido")
    })

    @GetMapping("/estado/{nombre}")
    public ResponseEntity<List<Atencion>> porEstado(
            @Parameter(description = "Nombre del estado", example = "realizada")
            @PathVariable String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Atencion> lista = atencionService.buscarPorEstado(nombre);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);

    }

}
