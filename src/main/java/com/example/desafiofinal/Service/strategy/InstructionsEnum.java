package com.example.desafiofinal.Service.strategy;

import java.util.Arrays;

public enum InstructionsEnum
{
    COLLECTSAMPLE, SCAN, DEPLOYROVER;

    public static InstructionsEnum fromString(String tipoInstruccion) {
        for (InstructionsEnum instruction : InstructionsEnum.values()) {
            if (instruction.name().equalsIgnoreCase(tipoInstruccion)) {
                return instruction;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + tipoInstruccion);
    }
}
