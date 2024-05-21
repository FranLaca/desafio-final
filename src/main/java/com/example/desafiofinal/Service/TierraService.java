package com.example.desafiofinal.Service;

import com.example.desafiofinal.model.Singleton.Tierra;
import com.example.desafiofinal.repository.TierraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TierraService
{
    private final TierraRepository tierraRepository;


    public Optional<Tierra> findById(Integer tierraId)
    {
        return tierraRepository.findById(tierraId);
    }
}
