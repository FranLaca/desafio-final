package com.example.desafiofinal.Service;

import com.example.desafiofinal.model.Instruction;
import com.example.desafiofinal.repository.InstructionRepository;
import com.example.desafiofinal.Service.strategy.InstructionsEnum;
import com.example.desafiofinal.Service.strategy.InstructionsStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InstructionService implements InitializingBean
{
    private final InstructionRepository instructionRepository;
    final private List<InstructionsStrategy> instructionsStrategies;
    private Map<InstructionsEnum,InstructionsStrategy> strategyMap;
    public InstructionService(InstructionRepository instructionRepository, List<InstructionsStrategy>instructionsStrategies)
    {
        this.instructionRepository = instructionRepository;
        this.instructionsStrategies=instructionsStrategies;
    }
    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.strategyMap = new HashMap<>();
        for (InstructionsStrategy strategy : instructionsStrategies)
        {
            strategyMap.put(strategy.getTipoInstruction(), strategy);
        }
    }
    public String executeInstruccion(Instruction instruction) {
        try {
            if (strategyMap.containsKey(instruction.getTipoInstruccion()))
            {
                return strategyMap.get(instruction.getTipoInstruccion()).execute();
            } else
            {
                throw new IllegalArgumentException("Invalid instruction type: " + instruction.getTipoInstruccion());
            }
        } catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Error executing instruction: " + e.getMessage());
        }
    }

    public void createInstruction(Instruction instruction)
    {
        instructionRepository.save(instruction);
    }
}
