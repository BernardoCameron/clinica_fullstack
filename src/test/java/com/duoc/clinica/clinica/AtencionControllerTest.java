package com.duoc.clinica.clinica;


import com.duoc.clinica.clinica.controller.AtencionController;
import com.duoc.clinica.clinica.model.Atencion;
import com.duoc.clinica.clinica.model.Estado;
import com.duoc.clinica.clinica.model.Medico;
import com.duoc.clinica.clinica.model.Paciente;
import com.duoc.clinica.clinica.service.AtencionService;
import com.duoc.clinica.clinica.service.MedicoService;
import com.duoc.clinica.clinica.service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AtencionController.class)

public class AtencionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtencionService atencionService;

    @MockBean
    private MedicoService medicoService;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Prueba que se pueda crear una atencion cuando los datos estan bien.
     */
    @Test
    void crearAtencion_ok() throws Exception {
        Atencion atencion = new Atencion();
        atencion.setFechaAtencion(LocalDate.of(2025, 7, 1));
        atencion.setHoraAtencion(LocalTime.of(10, 30));
        atencion.setComentario("Control general");
        atencion.setCosto(25000.0);

        Estado estado = new Estado();
        estado.setId(1L);
        atencion.setEstado(estado);

        Paciente paciente = new Paciente();
        paciente.setId(1L);
        atencion.setPaciente(paciente);

        Medico medico = new Medico();
        medico.setId(1L);
        atencion.setMedico(medico);

        when(atencionService.crearAtencion(any(Atencion.class))).thenReturn(atencion);

        String json = objectMapper.writeValueAsString(atencion);

        mockMvc.perform(post(URI.create("/api/v1/atenciones"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    /**
     * Prueba que si faltan datos obligatorios al crear una atencion,
     * el sistema devuelva 400 como respuesta.
     */
    @Test
    void crearAtencion_datosInvalidos() throws Exception {
        String json = """
        {
            "fechaAtencion": "",
            "horaAtencion":,
            "costo": null,
            "comentario": "",
            "estado": null,
            "paciente": null,
            "medico": null
        }
        """;

        mockMvc.perform(post(URI.create("/api/v1/atenciones"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que el endpoint de listar atenciones devuelva 200 cuando hay datos disponibles.
     */
    @Test
    void listarTodas_conResultados() throws Exception {
        Atencion atencion1 = new Atencion();
        atencion1.setId(1L);
        atencion1.setComentario("Ejemplo de atencion");
        atencion1.setCosto(50000.0);
        atencion1.setFechaAtencion(LocalDate.of(2023, 1, 1));
        atencion1.setHoraAtencion(LocalTime.of(10, 0));
        atencion1.setEstado(new Estado());
        atencion1.setMedico(new Medico());
        atencion1.setPaciente(new Paciente());

        when(atencionService.listarTodas()).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones"))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que el endpoint de listar atenciones devuelva 204 cuando no hay ninguna atencion registrada.
     */
    @Test
    void listarTodas_sinResultados() throws Exception {
        when(atencionService.listarTodas()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones"))
                .andExpect(status().isNoContent());
    }


    /**
     * Prueba que se devuelva 200 cuando se encuentran atenciones para un médico específico.
     */
    @Test
    void buscarPorMedico_conResultados() throws Exception {
        Atencion atencion = new Atencion();
        atencion.setId(1L);
        atencion.setComentario("Control de rutina");
        atencion.setCosto(30000.0);
        atencion.setFechaAtencion(LocalDate.of(2024, 10, 10));
        atencion.setHoraAtencion(LocalTime.of(14, 30));
        atencion.setEstado(new Estado());
        atencion.setPaciente(new Paciente());
        atencion.setMedico(new Medico());

        when(atencionService.buscarPorMedico(1L)).thenReturn(List.of(atencion));

        mockMvc.perform(get("/api/v1/atenciones/medico/{id}", 1L))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que se devuelva 204 cuando el médico existe pero no tiene atenciones registradas.
     */
    @Test
    void buscarPorMedico_sinResultados() throws Exception {
        when(atencionService.buscarPorMedico(99L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones/medico/{id}", 99L))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que se devuelva 400 si el ID del médico no es un número válido.
     */
    @Test
    void buscarPorMedico_idInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/atenciones/medico/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que se devuelva 200 cuando se encuentran atenciones para un paciente específico.
     */
    @Test
    void buscarPorPaciente_conResultados() throws Exception {
        Atencion atencion = new Atencion();
        atencion.setId(1L);
        atencion.setComentario("Chequeo general");
        atencion.setCosto(40000.0);
        atencion.setFechaAtencion(LocalDate.of(2024, 5, 15));
        atencion.setHoraAtencion(LocalTime.of(11, 0));
        atencion.setEstado(new Estado());
        atencion.setPaciente(new Paciente());
        atencion.setMedico(new Medico());

        when(atencionService.buscarPorPaciente(2L)).thenReturn(List.of(atencion));

        mockMvc.perform(get("/api/v1/atenciones/paciente/{id}", 2L))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que se devuelva 204 cuando el paciente no tiene atenciones.
     */
    @Test
    void buscarPorPaciente_sinResultados() throws Exception {
        when(atencionService.buscarPorPaciente(999L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones/paciente/{id}", 999L))
                .andExpect(status().isNoContent());
    }


    /**
     * Prueba que se devuelva 400 si el ID del paciente no es numérico.
     */
    @Test
    void buscarPorPaciente_idInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/atenciones/paciente/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que se devuelva 200 cuando se encuentran atenciones en una fecha específica.
     */
    @Test
    void buscarPorFecha_conResultados() throws Exception {
        Atencion atencion = new Atencion();
        atencion.setId(1L);
        atencion.setComentario("Control de rutina");
        atencion.setCosto(30000.0);
        atencion.setFechaAtencion(LocalDate.of(2024, 7, 1));
        atencion.setHoraAtencion(LocalTime.of(10, 0));
        atencion.setEstado(new Estado());
        atencion.setPaciente(new Paciente());
        atencion.setMedico(new Medico());

        when(atencionService.buscarPorFecha(LocalDate.of(2024, 7, 1))).thenReturn(List.of(atencion));

        mockMvc.perform(get("/api/v1/atenciones/fecha/2024-07-01"))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que se devuelva 204 si no hay atenciones en la fecha indicada.
     */
    @Test
    void buscarPorFecha_sinResultados() throws Exception {
        when(atencionService.buscarPorFecha(LocalDate.of(2023, 1, 1))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones/fecha/2023-01-01"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que se devuelva 400 si se envía una fecha con formato inválido.
     */
    @Test
    void buscarPorFecha_formatoInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/atenciones/fecha/01-07-2024"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que se devuelva 200 si hay atenciones dentro del rango de fechas.
     */
    @Test
    void buscarEntreFechas_conResultados() throws Exception {
        Atencion atencion = new Atencion();
        atencion.setId(1L);
        atencion.setComentario("Consulta general");
        atencion.setCosto(40000.0);
        atencion.setFechaAtencion(LocalDate.of(2024, 5, 15));
        atencion.setHoraAtencion(LocalTime.of(9, 0));
        atencion.setEstado(new Estado());
        atencion.setPaciente(new Paciente());
        atencion.setMedico(new Medico());

        when(atencionService.buscarEntreFechas(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 31)))
                .thenReturn(List.of(atencion));

        mockMvc.perform(get("/api/v1/atenciones/entre-fechas")
                        .param("desde", "2024-05-01")
                        .param("hasta", "2024-05-31"))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que se devuelva 204 si no hay atenciones en el rango indicado.
     */
    @Test
    void buscarEntreFechas_sinResultados() throws Exception {
        when(atencionService.buscarEntreFechas(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 31)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones/entre-fechas")
                        .param("desde", "2023-01-01")
                        .param("hasta", "2023-01-31"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que se devuelva 400 si alguna de las fechas tiene formato inválido.
     */
    @Test
    void buscarEntreFechas_fechasInvalidas() throws Exception {
        mockMvc.perform(get("/api/v1/atenciones/entre-fechas")
                        .param("desde", "01-01-2023") // Formato inválido
                        .param("hasta", "2023-12-31"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que se devuelva 200 cuando hay atenciones con costo menor al monto dado.
     */
    @Test
    void buscarPorCostoMenor_conResultados() throws Exception {
        Atencion atencion = new Atencion();
        atencion.setId(1L);
        atencion.setComentario("Control pediátrico");
        atencion.setCosto(30000.0);
        atencion.setFechaAtencion(LocalDate.of(2024, 4, 10));
        atencion.setHoraAtencion(LocalTime.of(10, 0));
        atencion.setEstado(new Estado());
        atencion.setPaciente(new Paciente());
        atencion.setMedico(new Medico());

        when(atencionService.buscarPorCostoMenorA(50000.0)).thenReturn(List.of(atencion));

        mockMvc.perform(get("/api/v1/atenciones/costo/menor-a/50000"))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que se devuelva 204 si no hay atenciones con costo menor al valor entregado.
     */
    @Test
    void buscarPorCostoMenor_sinResultados() throws Exception {
        when(atencionService.buscarPorCostoMenorA(10000.0)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones/costo/menor-a/10000"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que se devuelva 400 si el monto entregado no es un número válido.
     */
    @Test
    void buscarPorCostoMenor_parametroInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/atenciones/costo/menor-a/noesnumero"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que se devuelva 200 cuando hay atenciones con costo mayor al monto dado.
     */
    @Test
    void buscarPorCostoMayor_conResultados() throws Exception {
        Atencion atencion = new Atencion();
        atencion.setId(1L);
        atencion.setComentario("Cirugía menor");
        atencion.setCosto(150000.0);
        atencion.setFechaAtencion(LocalDate.of(2024, 5, 15));
        atencion.setHoraAtencion(LocalTime.of(9, 30));
        atencion.setEstado(new Estado());
        atencion.setPaciente(new Paciente());
        atencion.setMedico(new Medico());

        when(atencionService.buscarPorCostoMayorA(100000.0)).thenReturn(List.of(atencion));

        mockMvc.perform(get("/api/v1/atenciones/costo/mayor-a/100000"))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que se devuelva 204 si no hay atenciones con costo mayor al valor entregado.
     */
    @Test
    void buscarPorCostoMayor_sinResultados() throws Exception {
        when(atencionService.buscarPorCostoMayorA(500000.0)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones/costo/mayor-a/500000"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que se devuelva 400 si el monto entregado no es un número válido.
     */
    @Test
    void buscarPorCostoMayor_parametroInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/atenciones/costo/mayor-a/invalid"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Prueba que se devuelva 200 cuando hay atenciones con el estado indicado.
     */
    @Test
    void buscarPorEstado_conResultados() throws Exception {
        Atencion atencion = new Atencion();
        atencion.setId(1L);
        atencion.setComentario("Control post-operatorio");
        atencion.setCosto(45000.0);
        atencion.setFechaAtencion(LocalDate.of(2024, 6, 5));
        atencion.setHoraAtencion(LocalTime.of(10, 15));
        atencion.setEstado(new Estado());
        atencion.setPaciente(new Paciente());
        atencion.setMedico(new Medico());

        when(atencionService.buscarPorEstado("realizada")).thenReturn(List.of(atencion));

        mockMvc.perform(get("/api/v1/atenciones/estado/realizada"))
                .andExpect(status().isOk());
    }

    /**
     * Prueba que se devuelva 204 si no hay atenciones con el estado entregado.
     */
    @Test
    void buscarPorEstado_sinResultados() throws Exception {
        when(atencionService.buscarPorEstado("pendiente")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones/estado/pendiente"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que se devuelva 400 si el estado está vacío o no es válido.
     */
    @Test
    void buscarPorEstado_parametroInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/atenciones/estado/ "))
                .andExpect(status().isBadRequest());
    }


}
