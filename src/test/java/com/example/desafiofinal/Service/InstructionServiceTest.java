package com.example.desafiofinal.Service;

import com.example.desafiofinal.Service.strategy.CollectSample;
import com.example.desafiofinal.Service.strategy.DeployRover;
import com.example.desafiofinal.Service.strategy.InstructionsEnum;
import com.example.desafiofinal.Service.strategy.Scan;
import com.example.desafiofinal.model.Instruction;
import com.example.desafiofinal.repository.InstructionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructionServiceTest
{

    @Mock
    private InstructionRepository instructionRepository;

    @Mock
    private Scan scanStrategy;

    @Mock
    private DeployRover deployRoverStrategy;

    @Mock
    private CollectSample collectSampleStrategy;

    @InjectMocks
    private InstructionService instructionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(scanStrategy.getTipoInstruction()).thenReturn(InstructionsEnum.SCAN);
        when(deployRoverStrategy.getTipoInstruction()).thenReturn(InstructionsEnum.DEPLOYROVER);
        when(collectSampleStrategy.getTipoInstruction()).thenReturn(InstructionsEnum.COLLECTSAMPLE);

        instructionService = new InstructionService(instructionRepository, Arrays.asList(scanStrategy, deployRoverStrategy, collectSampleStrategy));
        instructionService.afterPropertiesSet();
    }

    @Test
    public void testExecuteInstruccion_Success() {
        Instruction instruction = new Instruction();
        instruction.setTipoInstruccion(InstructionsEnum.SCAN);

        when(scanStrategy.execute()).thenReturn("Scaneo Exitoso!");

        String result = instructionService.executeInstruccion(instruction);

        assertEquals("Scaneo Exitoso!", result);
        verify(scanStrategy, times(1)).execute();
    }

    @Test
    public void testExecuteInstruccion_InvalidType() {
        Instruction instruction = new Instruction();
        instruction.setTipoInstruccion(null);  // Set Null para simular dato invalido

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            instructionService.executeInstruccion(instruction);
        });

        assertEquals("Invalid instruction type: null", exception.getMessage());
    }

    @Test
    public void testObtenerDatos_Success() {
        when(scanStrategy.obtenerDatos()).thenReturn("Perimetro habilitado para explorar");

        String result = instructionService.obtenerDatos(InstructionsEnum.SCAN);

        assertEquals("Perimetro habilitado para explorar", result);
        verify(scanStrategy, times(1)).obtenerDatos();
    }

    @Test
    public void testObtenerDatos_InvalidType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            instructionService.obtenerDatos(null);  // Null para simular dato invalido
        });

        assertEquals("Invalid instruction type: null", exception.getMessage());
    }

    @Test
    public void testCreateInstruction() {
        Instruction instruction = new Instruction();

        instructionService.createInstruction(instruction);

        verify(instructionRepository, times(1)).save(instruction);
    }
}