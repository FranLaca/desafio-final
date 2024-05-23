package com.example.desafiofinal.Auth;

import com.example.desafiofinal.API.InstructionRequest;
import com.example.desafiofinal.API.TelemetryRequest;
import com.example.desafiofinal.Service.InstructionService;
import com.example.desafiofinal.Service.NaveService;
import com.example.desafiofinal.Service.TelemetryService;
import com.example.desafiofinal.Service.TierraService;
import com.example.desafiofinal.model.Instruction;
import com.example.desafiofinal.model.Singleton.Nave;
import com.example.desafiofinal.model.Singleton.Tierra;
import com.example.desafiofinal.model.Telemetry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
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
    @Autowired
    TelemetryService telemetryService;


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


    @GetMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (AuthenticationException e)
        {
            Map<String, String> response = new HashMap<>();
            response.put("status", "UNAUTHORIZED");
            response.put("code", "401");
            response.put("message", "Authentication failed: " + e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e)
        {
            Map<String, String> response = new HashMap<>();
            response.put("status", "BAD_REQUEST");
            response.put("code", "400");
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (HttpMessageNotReadableException e)
        {
            Map<String, String> response = new HashMap<>();
            response.put("status", "BAD_REQUEST");
            response.put("code", "400");
            response.put("message", "Revise los campos de la solicitud: " + e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e)
        {
            Map<String, String> response = new HashMap<>();
            response.put("status", "INTERNAL_SERVER_ERROR");
            response.put("code", "500");
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
