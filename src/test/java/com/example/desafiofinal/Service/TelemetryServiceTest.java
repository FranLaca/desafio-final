package com.example.desafiofinal.Service;

import com.example.desafiofinal.model.Telemetry;
import com.example.desafiofinal.repository.TelemetryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TelemetryServiceTest
{
    @Mock
    private TelemetryRepository telemetryRepository;

    @InjectMocks
    private TelemetryService telemetryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearTelemetry() {
        Telemetry telemetry = new Telemetry();

        telemetryService.CrearTelemetry(telemetry);

        verify(telemetryRepository, times(1)).save(telemetry);
    }
}