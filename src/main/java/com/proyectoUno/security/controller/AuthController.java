package com.proyectoUno.security.controller;

import com.proyectoUno.security.dto.AuthResponse;
import com.proyectoUno.security.dto.LoginRequest;
import com.proyectoUno.security.dto.Logout;
import com.proyectoUno.security.dto.RegisterRequest;
import com.proyectoUno.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController( AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public  ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){

        return  ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken){
        return  ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Logout request){

        authService.logout(request.accessToken(),request.refreshToken());
        return ResponseEntity.ok().build();
    }

}
