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
        return "Scaneo Exitoso!";

    }

    @Override
    public String obtenerDatos() {
        return "Perimetro habilitado para explorar";
    }
}
