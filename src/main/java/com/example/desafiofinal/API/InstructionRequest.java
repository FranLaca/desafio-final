package com.example.desafiofinal.API;

import com.example.desafiofinal.Service.strategy.InstructionsEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstructionRequest {
    private Integer tierraId;
    private Integer naveId;
    private InstructionsEnum tipoInstruccion;
}
