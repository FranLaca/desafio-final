package com.example.desafiofinal.Service.strategy;

import org.springframework.stereotype.Component;

@Component
public class CollectSample implements InstructionsStrategy
{
    @Override
    public InstructionsEnum getTipoInstruction() {
        return InstructionsEnum.COLLECTSAMPLE;
    }

    @Override
    public String execute()
    {
        return "Recogiendo muestras del suelo";
    }

    @Override
    public String obtenerDatos() {
        return "Se encontraron minerales";
    }
}
