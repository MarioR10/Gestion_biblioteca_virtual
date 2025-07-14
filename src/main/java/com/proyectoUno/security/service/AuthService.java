package com.proyectoUno.security.service;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadDuplicadaException;
import com.proyectoUno.maper.usuario.UsuarioRegisterMapper;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.security.dto.AuthResponse;
import com.proyectoUno.security.dto.LoginRequest;
import com.proyectoUno.security.dto.RegisterRequest;
import com.proyectoUno.security.jwt.JwtService;
import com.proyectoUno.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * Esta clase es la encargada de los procesos de autentificacion del usuario en el servidor,
 *  se encarga de: Login, Register, genera los token de acceso y refresh para el usuario
 */

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioRegisterMapper usuarioRegisterMapper;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, JwtService jwtService, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,UserDetailsService userDetailsService, UsuarioRegisterMapper usuarioRegisterMapper) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService= userDetailsService;
        this.usuarioRegisterMapper=usuarioRegisterMapper;
    }

    /**
     * Metodo para iniciar sesion, para usuario ya existentes en la base de datos
     * valida las credenciales y emite tokens para el usuario
     */
    public AuthResponse login(LoginRequest request){

        /*
        Creamos un AuthenticationManager y le pasamos las credenciales del usuario mediante un objeto
        UsernamePasswordAuthenticationToken() que encapsula las credenciales (un Authentication).
         */
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.contrasena())
        );

        //Cargamos los datos del usuario utilizando el servicio creado
        UserDetails userDetails = userDetailsService.loadUserByUsername( request.email());

        //Creamos token de acceso y refresh
        String jwtToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        //devolvemos el DTO de repsuesta con ambos token
        return new AuthResponse(jwtToken,refreshToken);

    }

    public AuthResponse register(RegisterRequest request){

        /*Hacemos una consulta a la base de datos, y verificamos si el correo del usuario esta presente,
        si es asi, mandamos una excepcion de que ya existe una cuenta con ese correo, si no seguimos con el proceso
        de registrarlo
         */

        if( usuarioRepository.findByEmail(request.email()).isPresent()){
            // Envolvemos el email único en una lista
            List<String> emailDuplicado = Collections.singletonList(request.email());
            throw new EntidadDuplicadaException("Email ya esta asociado a una cuenta", "email", emailDuplicado);
        }

        // Usamos el mapper para convertir el RegisterRequest a un Usuario.
        //Tambien se encargara internamente de codificar la contraseña que viene en el DTO
        Usuario usuario = usuarioRegisterMapper.toEntity(request,this.passwordEncoder);
        usuarioRepository.save(usuario);

        //Creamos el perfil en el contexto de Spring Security
        UserDetails userDetails= new CustomUserDetails(usuario);

        //Creamos tokens para el usuario
        String jwtToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new AuthResponse(jwtToken,refreshToken);


    }

    /*
    El metodo tiene como objetivo validar el refreshtoken existente y, si es valido emitir un nuevo access token junto con
    el refreshToken original (o uno nuevo si se implementa rotacion).De esta forma evitamos que el usuario tenga que iniciar sesion constantemente

     */
    public AuthResponse refreshToken(String refreshToken){

        //Obtenemos el username (email) del refresh token que nos mandan
        String username = jwtService.getUsernameFromToken(refreshToken);

        //Con el username obtenido, cargamos los datos del usuario (en el contexto de Spring Security0
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Validamos que aun sea valido el refresh token que nos mandan, para poder generar un nuevo access token
        if(jwtService.isTokenValid(refreshToken, userDetails)){
            String newJwtToken = jwtService.generateAccessToken(userDetails);
            return new AuthResponse(newJwtToken,refreshToken);
        }
        throw new RuntimeException("Refresh token invalido");
    }
}
