package com.example.desafiofinal.repository;

import com.example.desafiofinal.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository extends JpaRepository<Instruction,Integer>
{
}
