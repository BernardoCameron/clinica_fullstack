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

        mockMvc.perform(post(URI.create("/api/v1/medicos"))
                        .contentType("application/json")
                        .content("""
                {
                    "run": "12345678-9",
                    "nombre": "Pedro",
                    "apellido": "Ramirez",
                    "fechaIngreso": "2020-01-01",
                    "sueldoBase": 900000,
                    "correo": "pedro.ramirez@hospital.cl",
                    "telefono": "56912345678",
                    "especialidad": {
                        "id": 1
                    }
                }
            """))
                .andExpect(status().isCreated());
    }

    /**
     * Prueba que el endpoint POST /api/v1/medicos
     * retorne 400 Bad Request cuando el medico tiene datos invalidos.
     */
    @Test
    void guardarMedicoCamposInvalidos() throws Exception {
        Medico medico = new Medico();
        medico.setRun("12345678-9");
        medico.setApellido("Ramirez");
        medico.setFechaIngreso(LocalDate.of(2020, 1, 1));
        medico.setSueldoBase(900000.0);
        medico.setCorreo("pedro.ramirez@hospital.cl");
        medico.setTelefono("56912345678");

        Especialidad esp = new Especialidad();
        esp.setId(1L);
        medico.setEspecialidad(esp);

        String json = objectMapper.writeValueAsString(medico);

        mockMvc.perform(post(URI.create("/api/v1/medicos"))
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}
