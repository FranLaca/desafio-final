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
        return "Deploy exitoso!";
    }

    @Override
    public String obtenerDatos() {
        return "Coordenadas -85,456 , 120,365";
    }
}
