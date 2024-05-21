package com.example.desafiofinal.repository;

import com.example.desafiofinal.model.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryRepository extends JpaRepository<Telemetry,Integer>
{
}
