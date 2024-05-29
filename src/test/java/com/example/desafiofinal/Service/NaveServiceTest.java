package com.example.desafiofinal.Service;

import com.example.desafiofinal.model.Singleton.Nave;
import com.example.desafiofinal.repository.NaveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class NaveServiceTest
{
    @Mock
    private NaveRepository naveRepository;

    @InjectMocks
    private NaveService naveService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Nave nave = new Nave();
        nave.setId(1);

        when(naveRepository.findById(1)).thenReturn(Optional.of(nave));

        Optional<Nave> result = naveService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(naveRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Nave> result = naveService.findById(1);

        assertTrue(result.isEmpty());
    }
}