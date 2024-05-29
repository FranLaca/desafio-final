package com.example.desafiofinal.Service;

import com.example.desafiofinal.model.Singleton.Tierra;
import com.example.desafiofinal.repository.TierraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TierraServiceTest {
    @Mock
    private TierraRepository tierraRepository;

    @InjectMocks
    private TierraService tierraService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Tierra tierra = new Tierra();
        tierra.setId(1);

        when(tierraRepository.findById(1)).thenReturn(Optional.of(tierra));

        Optional<Tierra> result = tierraService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(tierraRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Tierra> result = tierraService.findById(1);

        assertTrue(result.isEmpty());
    }
}