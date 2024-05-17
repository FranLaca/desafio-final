package com.example.desafiofinal.Auth;

import lombok.RequiredArgsConstructor;
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
}
