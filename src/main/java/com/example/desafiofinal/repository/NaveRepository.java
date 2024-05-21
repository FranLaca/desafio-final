package com.example.desafiofinal.repository;

import com.example.desafiofinal.model.Singleton.Nave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaveRepository extends JpaRepository<Nave,Integer>
{
}
