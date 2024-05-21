package com.example.desafiofinal.Service.strategy;

import org.springframework.stereotype.Component;

@Component
public class DeployRover implements InstructionsStrategy
{
    @Override
    public InstructionsEnum getTipoInstruction() {
        return InstructionsEnum.DEPLOYROVER;
    }

    @Override
    public String execute()
    {
        return "deploy";
    }
}
