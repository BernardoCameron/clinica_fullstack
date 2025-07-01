package com.duoc.clinica.clinica;


import com.duoc.clinica.clinica.controller.PacienteController;
import com.duoc.clinica.clinica.model.Paciente;
import com.duoc.clinica.clinica.service.AtencionService;
import com.duoc.clinica.clinica.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;


    /**
     * Prueba que el endpoint GET /api/v1/pacientes
     * responde con estado 200 (OK) cuando existe al menos un paciente en la lista.
     *
     * Se mockea el servicio para devolver una lista con un paciente,
     * y se espera que el controlador retorne la respuesta exitosa.
     */

    @Test
    void listarPacienteExistente() throws Exception {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Juan");
        paciente.setApellido("Perez");
        paciente.setRun("12345678-9");
        paciente.setCorreo("juan@test.cl");
        paciente.setTelefono("123456789");
        paciente.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        List<Paciente> lista = new ArrayList<>();
        lista.add(paciente);

        when(pacienteService.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/pacientes"))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes
     * retorne 204 No Content cuando no hay pacientes registrados.
     */

    @Test
    void listarPacienteSinRegistros() throws Exception {
        List<Paciente> listaVacia = new ArrayList<>();
        when(pacienteService.listarTodos()).thenReturn(listaVacia);

        MvcResult resultado = mockMvc.perform(get("/api/v1/pacientes"))
                .andExpect(status().isNoContent())
                .andReturn();

        int status = resultado.getResponse().getStatus();
        if (status != 204) {
            fail("Se esperaba un estado 204 No Content, pero se obtuvo: " + status);
        }
    }


    /**
     * Prueba que el endpoint GET /api/v1/pacientes/buscar/{run}
     * retorne 200 OK cuando el paciente existe.
     */
    @Test
    void buscarPacientePorRunExistente() throws Exception {
        String run = "15123588-2";
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setRun(run);
        paciente.setNombre("Maria");
        paciente.setApellido("Sanchez");

        when(pacienteService.buscarPorRun(run)).thenReturn(Optional.of(paciente));

        mockMvc.perform(get("/api/v1/pacientes/buscar/" + run))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.run").value(run))
                .andExpect(jsonPath("$.nombre").value("Maria"))
                .andExpect(jsonPath("$.apellido").value("Sanchez"));
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/buscar/{run}
     * retorne 404 Not Found cuando el paciente no existe.
     */
    @Test
    void buscarPacientePorRunInexistente() throws Exception {
        String run = "99999999-9";
        when(pacienteService.buscarPorRun(run)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/pacientes/buscar/" + run))
                .andExpect(status().isNotFound());
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/buscar?nombre=...&apellido=...
     * retorne 200 OK cuando hay coincidencias.
     */
    @Test
    void buscarPacientePorNombreYApellidoExistente() throws Exception {
        String nombre = "Maria";
        String apellido = "Sanchez";

        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);

        List<Paciente> resultado = List.of(paciente);
        when(pacienteService.buscarPorNombreYApellido(nombre, apellido)).thenReturn(resultado);

        mockMvc.perform(get("/api/v1/pacientes/buscar")
                        .param("nombre", nombre)
                        .param("apellido", apellido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value(nombre))
                .andExpect(jsonPath("$[0].apellido").value(apellido));
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/buscar?nombre=...&apellido=...
     * retorne 204 No Content cuando no hay coincidencias.
     */
    @Test
    void buscarPacientePorNombreYApellidoSinResultados() throws Exception {
        String nombre = "NombreFalso";
        String apellido = "ApellidoFalso";

        when(pacienteService.buscarPorNombreYApellido(nombre, apellido)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/pacientes/buscar")
                        .param("nombre", nombre)
                        .param("apellido", apellido))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/buscar/edad/menor-a/{edad}
     * retorne 200 OK cuando existen pacientes menores a la edad dada.
     */
    @Test
    void buscarPacientesMenoresDeEdad_conResultados() throws Exception {
        int edad = 40;
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Camila");
        paciente.setApellido("Torres");

        when(pacienteService.pacientesMenoresDe(edad)).thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/v1/pacientes/buscar/edad/menor-a/{edad}", edad))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Camila"))
                .andExpect(jsonPath("$[0].apellido").value("Torres"));
    }
    /**
     * Prueba que el endpoint GET /api/v1/pacientes/buscar/edad/menor-a/{edad}
     * retorne 204 No Content cuando no existen pacientes menores a la edad dada.
     */
    @Test
    void buscarPacientesMenoresDeEdad_sinResultados() throws Exception {
        int edad = 1;

        when(pacienteService.pacientesMenoresDe(edad)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/pacientes/buscar/edad/menor-a/{edad}", edad))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/buscar/edad/mayor-a/{edad}
     * retorne 200 OK cuando existen pacientes mayores a la edad dada.
     */
    @Test
    void buscarPacientesMayoresDeEdad_conResultados() throws Exception {
        int edad = 30;

        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Jorge");
        paciente.setApellido("Ramirez");

        when(pacienteService.pacientesMayoresDe(edad)).thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/v1/pacientes/buscar/edad/mayor-a/{edad}", edad))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Jorge"))
                .andExpect(jsonPath("$[0].apellido").value("Ramirez"));
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/buscar/edad/mayor-a/{edad}
     * retorne 204 No Content cuando no existen pacientes mayores a la edad dada.
     */
    @Test
    void buscarPacientesMayoresDeEdad_sinResultados() throws Exception {
        int edad = 120;

        when(pacienteService.pacientesMayoresDe(edad)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/pacientes/buscar/edad/mayor-a/{edad}", edad))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/deuda/{id}
     * retorne 200 OK con el monto correcto cuando el paciente tiene deuda.
     */
    @Test
    void calcularDeudaPaciente_existente() throws Exception {
        Long idPaciente = 1L;
        double deuda = 15000;

        when(pacienteService.calcularDeudaPaciente(idPaciente)).thenReturn(deuda);

        mockMvc.perform(get("/api/v1/pacientes/deuda/{id}", idPaciente))
                .andExpect(status().isOk())
                .andExpect(content().string("15000.0"));
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/deuda/{id}
     * retorne 200 OK con 0.0 cuando el paciente no tiene deuda.
     */
    @Test
    void calcularDeudaPaciente_sinDeuda() throws Exception {
        Long idPaciente = 2L;

        when(pacienteService.calcularDeudaPaciente(idPaciente)).thenReturn(0.0);

        mockMvc.perform(get("/api/v1/pacientes/deuda/{id}", idPaciente))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/prevision/{nombre}
     * retorne 200 OK cuando existen pacientes con la prevision indicada.
     */
    @Test
    void pacientesPorPrevision_existente() throws Exception {
        String prevision = "FONASA";

        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Maria");
        paciente.setApellido("Sanchez");

        when(pacienteService.pacientesPorPrevision(prevision)).thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/v1/pacientes/prevision/{nombre}", prevision))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Maria"))
                .andExpect(jsonPath("$[0].apellido").value("Sanchez"));
    }

    /**
     * Prueba que el endpoint GET /api/v1/pacientes/prevision/{nombre}
     * retorne 204 No Content cuando no hay pacientes con esa prevision.
     */
    @Test
    void pacientesPorPrevision_sinResultados() throws Exception {
        String prevision = "CAPREDENA";

        when(pacienteService.pacientesPorPrevision(prevision)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/pacientes/prevision/{nombre}", prevision))
                .andExpect(status().isNoContent());
    }



}