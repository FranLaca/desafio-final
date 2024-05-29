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


    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (AuthenticationException e)
        {
            return handleException(HttpStatus.UNAUTHORIZED, "Authentication failed: " + e.getMessage());
        } catch (IllegalArgumentException e)
        {
            return handleException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (HttpMessageNotReadableException e)
        {
            return handleException(HttpStatus.BAD_REQUEST, "Revise los campos de la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return handleException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (IllegalArgumentException e)
        {
            return handleException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (HttpMessageNotReadableException e)
        {
            return handleException(HttpStatus.BAD_REQUEST, "Revise los campos de la solicitud: " + e.getMessage());
        } catch (DataIntegrityViolationException e)
        {
            return handleException(HttpStatus.INTERNAL_SERVER_ERROR, "Data integrity violation: " + e.getMessage());
        } catch (Exception e)
        {
            return handleException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private ResponseEntity<AuthResponse> handleException(HttpStatus status, String message)
    {
        Map<String, String> response = new HashMap<>();
        response.put("status", status.getReasonPhrase());
        response.put("code", String.valueOf(status.value()));
        response.put("message", message);
        return ResponseEntity.status(status).body(new AuthResponse(null, message));
    }
}
