package com.example.desafiofinal.Auth;

import com.example.desafiofinal.API.InstructionRequest;
import com.example.desafiofinal.Service.InstructionService;
import com.example.desafiofinal.Service.NaveService;
import com.example.desafiofinal.Service.TierraService;
import com.example.desafiofinal.model.Instruction;
import com.example.desafiofinal.model.Singleton.Nave;
import com.example.desafiofinal.model.Singleton.Tierra;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{
    @Autowired
    InstructionService instructionService;
    @Autowired
    NaveService naveService;
    @Autowired
    TierraService tierraService;


    private final AuthService authService;
    @GetMapping(value = "/status")
    @ResponseBody
    public ResponseEntity<Map> status()
    {
        Map response = new HashMap();
        response.put("status", "OK");
        response.put("code", "200");
        response.put("message", "online");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/instructions")
    public ResponseEntity<Map<String, String>> receiveInstructions(@RequestBody InstructionRequest request) {
        try
        {
            Tierra tierra = tierraService.findById(request.getTierraId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Tierra ID: " + request.getTierraId()));
            Nave nave = naveService.findById(request.getNaveId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Nave ID: " + request.getNaveId()));

            Instruction instruction = new Instruction();
            instruction.setTierra(tierra);
            instruction.setNave(nave);
            instruction.setTipoInstruccion(request.getTipoInstruccion());


            instructionService.createInstruction(instruction);
            String respuesta =instructionService.executeInstruccion(instruction);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Instruccion envida correctamente");
            response.put("Tipo de instruccion", String.valueOf(instruction.getTipoInstruccion()));
            response.put("Tipo de respuesta",respuesta);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e)
        {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
