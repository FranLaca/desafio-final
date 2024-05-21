package com.example.desafiofinal.Service.strategy;

import org.springframework.stereotype.Component;

@Component
public class Scan implements InstructionsStrategy
{
    @Override
    public InstructionsEnum getTipoInstruction() {
        return InstructionsEnum.SCAN;
    }

    @Override
    public String execute()
    {
        return "Scan Exitoso";

    }
}
