package com.example.desafiofinal.API;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class ApiRest {
    @GetMapping(value = "/status")
    @ResponseBody
    @PreAuthorize("hasRole('TIERRA')")
    public ResponseEntity<Map> status() {
        Map response = new HashMap();
        response.put("status", "OK");
        response.put("code", "200");
        response.put("message", "online");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/telemetry")
    @ResponseBody
    public ResponseEntity<Map<String,String>>sendTelemetria(){
        return null;
    }
    @PostMapping(value = "/instructions")
    @ResponseBody
    public ResponseEntity<Map<String,String>>reciveIntrucciones(){
        return null;
    }
}
