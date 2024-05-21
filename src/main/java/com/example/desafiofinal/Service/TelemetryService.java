package com.example.desafiofinal.Service;

import com.example.desafiofinal.model.Telemetry;
import com.example.desafiofinal.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelemetryService
{
    private final TelemetryRepository telemetryRepository;

    public void CrearTelemetry (Telemetry telemetry){telemetryRepository.save(telemetry);}
}
