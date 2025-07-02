package com.duoc.clinica.clinica;


import com.duoc.clinica.clinica.controller.MedicoController;
import com.duoc.clinica.clinica.model.Especialidad;
import com.duoc.clinica.clinica.model.Medico;
import com.duoc.clinica.clinica.service.MedicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.validation.Validator;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicoController.class)
public class MedicoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicoService medicoService;

    /**
     * Prueba que el endpoint POST /api/v1/medicos
     * retorne 201 Created cuando se crea un medico con datos validos.
     */

    @Test
    void guardarMedicoOk() throws Exception {
        Medico medico = new Medico();
        medico.setRun("12345678-9");
        medico.setNombre("Pedro");
        medico.setApellido("Ramirez");
        medico.setFechaIngreso(LocalDate.of(2020, 1, 1));
        medico.setSueldoBase(900000.0);
        medico.setCorreo("pedro.ramirez@hospital.cl");
        medico.setTelefono("56912345678");

        Especialidad esp = new Especialidad();
        esp.setId(1L);
        medico.setEspecialidad(esp);

        when(medicoService.crearMedico(any(Medico.class))).thenReturn(medico);

        String json = objectMapper.writeValueAsString(medico);

        mockMvc.perform(post(URI.create("/api/v1/medicos"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    /**
     * Prueba que el endpoint POST /api/v1/medicos
     * retorne 400 Bad Request cuando el medico tiene datos invalidos.
     */


    @Test
    void guardarMedicoCamposInvalidos() throws Exception {
        // Creamos el JSON manualmente con el campo "nombre" vacío
        String medicoJson = """
        {
            "run": "12345678-9",
            "nombre": ,
            "apellido": "Ramirez",
            "fechaIngreso": "2020-01-01",
            "sueldoBase": 900000,
            "correo": "pedro.ramirez@hospital.cl",
            "telefono": "56912345678",
            "especialidad": {
                "id": 1
            }
        }
    """;

        mockMvc.perform(post(URI.create("/api/v1/medicos"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicoJson))
                .andExpect(status().isBadRequest());
    }



    /**
     * Prueba que el endpoint GET /api/v1/medicos
     * retorne 200 OK cuando hay medicos registrados.
     */
    @Test
    void listarMedicosConResultados() throws Exception {
        Medico medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Julio");
        medico.setApellido("Perez");

        when(medicoService.listarTodos()).thenReturn(List.of(medico));

        mockMvc.perform(get("/api/v1/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Julio"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"));
    }

    /**
     * Prueba que el endpoint GET /api/v1/medicos
     * retorne 204 No Content cuando no hay medicos registrados.
     */
    @Test
    void listarMedicosSinResultados() throws Exception {
        when(medicoService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/medicos"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que buscar por nombre y apellido devuelva 200 y lista si hay coincidencia.
     */
    @Test
    void buscarPorNombreYApellidoConResultado() throws Exception {
        Medico medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Juan");
        medico.setApellido("Perez");

        when(medicoService.buscarPorNombreYApellido("Juan", "Perez"))
                .thenReturn(List.of(medico));

        mockMvc.perform(get("/api/v1/medicos/buscar")
                        .param("nombre", "Juan")
                        .param("apellido", "Perez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"));
    }

    /**
     * Prueba que si no hay medicos con ese nombre y apellido,
     * se devuelva 204 sin contenido.
     */
    @Test
    void buscarPorNombreYApellidoSinResultado() throws Exception {
        when(medicoService.buscarPorNombreYApellido("NombreFalso", "ApellidoFalso"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/medicos/buscar")
                        .param("nombre", "NombreFalso")
                        .param("apellido", "ApellidoFalso"))
                .andExpect(status().isNoContent());
    }


    /**
     * Prueba que si se mandan parametros vacios, el sistema devuelva 400.
     */
    @Test
    void buscarPorNombreYApellidoCamposVacios() throws Exception {
        mockMvc.perform(get("/api/v1/medicos/buscar")
                        .param("nombre", "")
                        .param("apellido", ""))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que si hay medicos con menos años de antiguedad,
     * el sistema devuelva 200 con la lista.
     */
    @Test
    void listarMedicosConAntiguedadMenorAConResultado() throws Exception {
        Medico medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Laura");
        medico.setApellido("Soto");

        when(medicoService.conAntiguedadMenorA(5)).thenReturn(List.of(medico));

        mockMvc.perform(get("/api/v1/medicos/antiguedad/menor-a/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Laura"))
                .andExpect(jsonPath("$[0].apellido").value("Soto"));
    }

    /**
     * Prueba que si no hay medicos con menos años de antiguedad,
     * devuelva 204 sin contenido.
     */
    @Test
    void listarMedicosConAntiguedadMenorASinResultado() throws Exception {
        when(medicoService.conAntiguedadMenorA(2)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/medicos/antiguedad/menor-a/2"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que si se entrega un valor negativo como anios,
     * el sistema devuelva 400 porque no es valido.
     */
    @Test
    void listarMedicosConAntiguedadMenorAParametroInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/medicos/antiguedad/menor-a/-1"))
                .andExpect(status().isBadRequest());
    }


    /**
     * Prueba que si hay medicos con mas años de antiguedad,
     * devuelva 200 y lista de resultados.
     */
    @Test
    void listarMedicosConAntiguedadMayorAConResultado() throws Exception {
        Medico medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Carlos");
        medico.setApellido("Lopez");

        when(medicoService.conAntiguedadMayorA(10)).thenReturn(List.of(medico));

        mockMvc.perform(get("/api/v1/medicos/antiguedad/mayor-a/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Carlos"))
                .andExpect(jsonPath("$[0].apellido").value("Lopez"));
    }

    /**
     * Prueba que si no hay medicos con mas años de antiguedad,
     * el sistema devuelva 204 sin contenido.
     */
    @Test
    void listarMedicosConAntiguedadMayorASinResultado() throws Exception {
        when(medicoService.conAntiguedadMayorA(15)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/medicos/antiguedad/mayor-a/15"))
                .andExpect(status().isNoContent());
    }


    /**
     * Prueba que si se entrega un valor no valido en los años,
     * se devuelva 400 como respuesta.
     */
    @Test
    void listarMedicosConAntiguedadMayorAParametroInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/medicos/antiguedad/mayor-a/0"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que si el medico existe, se calcule y devuelva su sueldo total.
     */
    @Test
    void calcularSueldoTotalConResultado() throws Exception {
        Long id = 1L;
        Double sueldo = 1200000.0;

        when(medicoService.calcularSueldoTotal(id)).thenReturn(sueldo);

        mockMvc.perform(get("/api/v1/medicos/sueldo/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(sueldo.toString()));
    }

    /**
     * Prueba que si no se encuentra el medico, se devuelva 404.
     */
    @Test
    void calcularSueldoTotalMedicoNoEncontrado() throws Exception {
        Long id = 999L;

        when(medicoService.calcularSueldoTotal(id)).thenReturn(null);

        mockMvc.perform(get("/api/v1/medicos/sueldo/{id}", id))
                .andExpect(status().isNotFound());
    }

    /**
     * Prueba que si se entrega un ID invalido (como negativo), devuelva 400.
     */
    @Test
    void calcularSueldoTotalParametroInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/medicos/sueldo/{id}", -5))
                .andExpect(status().isBadRequest());
    }

}
