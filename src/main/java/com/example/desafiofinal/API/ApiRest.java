package com.example.desafiofinal.API;

import com.example.desafiofinal.Service.InstructionService;
import com.example.desafiofinal.Service.NaveService;
import com.example.desafiofinal.Service.TelemetryService;
import com.example.desafiofinal.Service.TierraService;
import com.example.desafiofinal.model.Instruction;
import com.example.desafiofinal.model.Singleton.Nave;
import com.example.desafiofinal.model.Singleton.Tierra;
import com.example.desafiofinal.model.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class ApiRest {
    @Autowired
    InstructionService instructionService;
    @Autowired
    NaveService naveService;
    @Autowired
    TierraService tierraService;
    @Autowired
    TelemetryService telemetryService;


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
    @PostMapping("/instructions")
    @PreAuthorize("hasRole('TIERRA')")
    public ResponseEntity<Map<String, String>> receiveInstructions(@RequestBody InstructionRequest request)
    {
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
        }
        catch (IllegalArgumentException e)
        {
            return handleIllegalArgumentException(e);
        }
        catch (HttpMessageNotReadableException e)
        {
            return handleBadRequestException(e);
        }
        catch (AuthenticationException e)
        {
            return handleAuthenticationException(e);
        }
        catch (DataIntegrityViolationException e)
        {
            return handleInternalServerErrorUnique(e, "identificadores no validos.");
        }
        catch (Exception e)
        {
            return handleInternalServerError(e);
        }
    }
    @GetMapping ("/telemetry")
    @PreAuthorize("hasRole('TIERRA')")
    public ResponseEntity<Map<String, String>> receiveTelemetry(@RequestBody TelemetryRequest request)
    {
        try
        {
            Tierra tierra = tierraService.findById(request.getTierraId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Tierra ID: " + request.getTierraId()));
            Nave nave = naveService.findById(request.getNaveId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Nave ID: " + request.getNaveId()));
            String datos =instructionService.obtenerDatos(request.getTipoDato());
            Telemetry telemetry = new Telemetry();
            telemetry.setTierra(tierra);
            telemetry.setNave(nave);
            telemetry.setTipoDato(request.getTipoDato());
            telemetry.setDatos(datos);
            telemetryService.CrearTelemetry(telemetry);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Telemetria enviada con exito");
            response.put("Solicitud de telemetria", String.valueOf(telemetry.getTipoDato()));
            response.put("Contenido de telemetria",datos);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        catch (IllegalArgumentException e)
        {
            return handleIllegalArgumentException(e);
        }
        catch (HttpMessageNotReadableException e)
        {
            return handleBadRequestException(e);
        }
        catch (DataIntegrityViolationException e)
        {
            return handleInternalServerErrorUnique(e, "identificadores no validos.");
        }
        catch (AuthenticationException e)
        {
            return handleAuthenticationException(e);
        }
        catch (Exception e)
        {
            return handleInternalServerError(e);
        }
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleBadRequestException(HttpMessageNotReadableException ex)
    {
        Map<String, String> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");
        response.put("code", "400");
        response.put("message", "Revise los campos de la solicitud: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleInternalServerErrorUnique(Exception ex, String message)
    {
        Map<String, String> response = new HashMap<>();
        response.put("status", "INTERNAL_SERVER_ERROR");
        response.put("code", "500");
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleInternalServerError(Exception ex)
    {
        Map<String, String> response = new HashMap<>();
        response.put("status", "INTERNAL_SERVER_ERROR");
        response.put("code", "500");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex)
    {
        Map<String, String> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");
        response.put("code", "400");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "FORBIDDEN");
        response.put("code", "403");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
