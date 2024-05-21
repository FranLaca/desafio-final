package com.example.desafiofinal.API;

import com.example.desafiofinal.Service.InstructionService;
import com.example.desafiofinal.model.Instruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class ApiRest {
    @Autowired
    InstructionService instructionService;


    @GetMapping(value = "/status")
    @ResponseBody
    @PreAuthorize("hasRole('TIERRA')")
    public ResponseEntity<Map> status()
    {
        Map response = new HashMap();
        response.put("status", "OK");
        response.put("code", "200");
        response.put("message", "online");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/telemetry")
    public ResponseEntity<Map<String, String>> receiveTelemetry()
    {

        Map<String, String> response = new HashMap<>();
        response.put("message", "Telemetry no Implementadda");
        return new ResponseEntity<>(response, HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/instructions")
    @PreAuthorize("hasRole('TIERRA')")
    public ResponseEntity<Map<String, String>> receiveInstructions(@RequestBody Instruction instruction) throws IOException
    {
        instructionService.executeInstruccion(instruction);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Instructions.");
        response.put("Tipo instruccion", instruction.getTipoInstruccion().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
