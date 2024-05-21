package com.example.desafiofinal.Service;

import com.example.desafiofinal.model.Singleton.Nave;
import com.example.desafiofinal.repository.NaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NaveService
{
    private final NaveRepository naveRepository;

    public Optional<Nave> findById(Integer naveId)
    {
        return  naveRepository.findById(naveId);
    }
}
