package com.example.desafiofinal.Service;

import com.example.desafiofinal.model.Instruction;
import com.example.desafiofinal.repository.InstructionRepository;
import com.example.desafiofinal.Service.strategy.InstructionsEnum;
import com.example.desafiofinal.Service.strategy.InstructionsStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InstructionService implements InitializingBean
{
    private final InstructionRepository instructionRepository;
    final private List<InstructionsStrategy> instructionsStrategies;
    private Map<InstructionsEnum,InstructionsStrategy> strategyMap;

    @Override
    public void afterPropertiesSet()
    {
        this.strategyMap = new HashMap<>();
        for (InstructionsStrategy strategy : instructionsStrategies)
        {
            strategyMap.put(strategy.getTipoInstruction(), strategy);
        }
    }
    public String executeInstruccion(Instruction instruction) {

            InstructionsEnum tipoInstruccion = instruction.getTipoInstruccion();
            if (strategyMap.containsKey(instruction.getTipoInstruccion()))
            {
                return strategyMap.get(instruction.getTipoInstruccion()).execute();
            } else
            {
                throw new IllegalArgumentException("Invalid instruction type: " + tipoInstruccion);
            }

    }
    public String obtenerDatos(InstructionsEnum instructionsEnum) {

            if (strategyMap.containsKey(instructionsEnum))
            {
                return strategyMap.get(instructionsEnum).obtenerDatos();
            } else
            {
                throw new IllegalArgumentException("Invalid instruction type: " + instructionsEnum);
            }
        }


    public void createInstruction(Instruction instruction)
    {
        instructionRepository.save(instruction);
    }
}
