package com.proyectoUno.security.controller;

import com.proyectoUno.security.dto.*;
import com.proyectoUno.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar la autenticación y el ciclo de vida de los tokens JWT.
 * Todas las rutas están bajo el prefijo "/auth".
 * Aquí se definen los endpoints principales de autenticación:
 * - Login
 * - Registro de usuarios
 * - Renovación de tokens (refresh)
 * - Logout
 * Este controlador delega la lógica en la capa de servicio (AuthService).
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController( AuthService authService){
        this.authService=authService;
    }

    /**
     * Endpoint de login.
     * - Recibe las credenciales del usuario (email y contraseña).
     * - Si son correctas, AuthService genera un par de tokens (access y refresh).
     * - Devuelve una respuesta con dichos tokens y la información básica del usuario.
     * @param request objeto con email y contraseña.
     * @return AuthResponse con los tokens JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){

        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Endpoint de registro de usuarios.
     * - Recibe los datos de un nuevo usuario (nombre, email, contraseña, etc.).
     * - AuthService se encarga de crear el usuario, encriptar su contraseña y asignar roles.
     * - Devuelve los tokens generados automáticamente al registrar al usuario.
     * @param request datos del nuevo usuario.
     * @return AuthResponse con los tokens JWT del nuevo usuario registrado.
     */
    @PostMapping("/register")
    public  ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){

        return  ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint para refrescar el token de acceso.
     * - Recibe un refresh token válido.
     * - AuthService valida el refresh token y, si es correcto, genera un nuevo access token.
     * @param refreshToken request con el refresh token.
     * @return AuthResponse con el nuevo access token (y opcionalmente un refresh token).
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken){
        return  ResponseEntity.ok(authService.refreshToken(refreshToken.refreshToken()));
    }

    /**
     * Endpoint de logout.
     * - Recibe los tokens (access y refresh).
     * - AuthService invalida los tokens, eliminándolos de la lista blanca / base de datos.
     * - Devuelve un 200 OK sin cuerpo.
     * @param request contiene accessToken y refreshToken.
     * @return ResponseEntity vacío con estado HTTP 200.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Logout request){

        authService.logout(request.accessToken(),request.refreshToken());
        return ResponseEntity.ok().build();
    }

}
