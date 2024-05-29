package com.example.desafiofinal.API;

import com.example.desafiofinal.Service.InstructionService;
import com.example.desafiofinal.Service.NaveService;
import com.example.desafiofinal.Service.TelemetryService;
import com.example.desafiofinal.Service.TierraService;
import com.example.desafiofinal.Service.strategy.InstructionsEnum;
import com.example.desafiofinal.model.Instruction;
import com.example.desafiofinal.model.Singleton.Nave;
import com.example.desafiofinal.model.Singleton.Tierra;
import com.example.desafiofinal.model.Telemetry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiRestTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstructionService instructionService;

    @MockBean
    private NaveService naveService;

    @MockBean
    private TelemetryService telemetryService;

    @MockBean
    private TierraService tierraService;
    @InjectMocks
    private ApiRest apiRestController;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(apiRestController).build();

        when(tierraService.findById(1)).thenReturn(Optional.of(new Tierra()));
        when(naveService.findById(1)).thenReturn(Optional.of(new Nave()));
        when(instructionService.executeInstruccion(any(Instruction.class))).thenReturn("Success");
    }

    @Test
    @WithMockUser(roles = "TIERRA")
    public void testStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("online"));
    }

    @Test
    @WithMockUser(roles = "TIERRA")
    public void testReceiveInstructions() throws Exception {
        InstructionRequest request = new InstructionRequest();
        request.setTierraId(1);
        request.setNaveId(1);
        request.setTipoInstruccion(InstructionsEnum.valueOf("COLLECTSAMPLE"));

        Tierra mockTierra = new Tierra();
        Nave mockNave = new Nave();
        Instruction mockInstruction = new Instruction();
        mockInstruction.setTierra(mockTierra);
        mockInstruction.setNave(mockNave);
        mockInstruction.setTipoInstruccion(InstructionsEnum.COLLECTSAMPLE);

        given(tierraService.findById(1)).willReturn(Optional.of(mockTierra));
        given(naveService.findById(1)).willReturn(Optional.of(mockNave));
        given(instructionService.executeInstruccion(any(Instruction.class))).willReturn("Success");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/instructions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tierraId\":1,\"naveId\":1,\"tipoInstruccion\":\"COLLECTSAMPLE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Instruccion envida correctamente"))
                .andExpect(jsonPath("$.['Tipo de instruccion']").value("COLLECTSAMPLE"))
                .andExpect(jsonPath("$.['Tipo de respuesta']").value("Success"));
    }

    @Test
    @WithMockUser(roles = "TIERRA")
    public void testReceiveTelemetry() throws Exception {
        // Crea la solicitud de telemetría
        TelemetryRequest request = new TelemetryRequest();
        request.setTierraId(1);
        request.setNaveId(1);
        request.setTipoDato(InstructionsEnum.valueOf("SCAN"));

        // Crea las instancias mock de tierra, nave y telemetría
        Tierra mockTierra = new Tierra();
        Nave mockNave = new Nave();
        Telemetry mockTelemetry = new Telemetry();
        mockTelemetry.setTierra(mockTierra);
        mockTelemetry.setNave(mockNave);
        mockTelemetry.setTipoDato(InstructionsEnum.SCAN);
        mockTelemetry.setDatos("Telemetry data");

        // Define el comportamiento esperado para los mocks
        given(tierraService.findById(1)).willReturn(Optional.of(mockTierra));
        given(naveService.findById(1)).willReturn(Optional.of(mockNave));
        given(instructionService.obtenerDatos(InstructionsEnum.SCAN)).willReturn("Telemetry data");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tierraId\":1,\"naveId\":1,\"tipoDato\":\"SCAN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Telemetria enviada con exito"))
                .andExpect(jsonPath("$.['Solicitud de telemetria']").value("SCAN"))
                .andExpect(jsonPath("$.['Contenido de telemetria']").value("Telemetry data"));

        verify(telemetryService, times(1)).CrearTelemetry(mockTelemetry);
    }
    @Test
    public void testHandleBadRequestException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/instructions"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").exists());
    }
    @Test
    public void testHandleIllegalArgumentException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/telemetry?tierraId=invalid_id"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").exists());
    }
}