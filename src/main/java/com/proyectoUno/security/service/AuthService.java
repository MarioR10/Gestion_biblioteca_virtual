package com.proyectoUno.security.service;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadDuplicadaException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.security.dto.AuthResponse;
import com.proyectoUno.security.dto.LoginRequest;
import com.proyectoUno.security.dto.RegisterRequest;
import com.proyectoUno.security.entity.RefreshToken;
import com.proyectoUno.security.exception.JwtRevokedException;
import com.proyectoUno.security.jwt.JwtService;
import com.proyectoUno.security.mapper.UsuarioRegisterMapper;
import com.proyectoUno.security.model.CustomUserDetails;
import com.proyectoUno.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Servicio encargado de los procesos de autenticación y registro de usuarios.
 * Sus responsabilidades principales son:
 * 1. Login de usuarios existentes y emisión de JWT (access y refresh token).
 * 2. Registro de nuevos usuarios con codificación de contraseña.
 * 3. Renovación de tokens mediante refresh tokens válidos.
 * 4. Logout y revocación de tokens.
 */

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioRegisterMapper usuarioRegisterMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlackListService tokenBlackListService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, JwtService jwtService, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UsuarioRegisterMapper usuarioRegisterMapper,
                       RefreshTokenRepository refreshTokenRepository, TokenBlackListService tokenBlackListService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService= userDetailsService;
        this.usuarioRegisterMapper = usuarioRegisterMapper;
        this.refreshTokenRepository =refreshTokenRepository;
        this.tokenBlackListService=tokenBlackListService;
    }

    /**
     * Autentica a un usuario existente y genera los tokens JWT.
     * @param request DTO con las credenciales del usuario.
     * @return AuthResponse con accessToken y refreshToken.
     */
    @Transactional
    public AuthResponse login(LoginRequest request){

        logger.debug("Iniciando autenticacion para el usuario (login)");
         /*
        Creamos un AuthenticationManager y le pasamos las credenciales del usuario mediante un objeto
        UsernamePasswordAuthenticationToken() que encapsula las credenciales (un Authentication).
         */
         Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.contrasena()));

         //Obtenemos el UserDetails del Authentication (asi evitamos hacer una segunda consulta a la base de datos)
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();

         //Obtenemos la entidad Usuario del UserDetails (asi evitamos un tercer consulta a la base de datos)
         Usuario usuario = ((CustomUserDetails) userDetails).getUsuario();

        //Creamos token de acceso y refresh
        String jwtToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        /*
        Guardamos en refresh Token en la base de datos.
        1. Eliminamos cualquier refresh Token activo para este usuario si es Admin o Moderator, de esta manera
        nos aseguramos que haya una sesion activa por usuario. Si es un usuario comun "User" le
        permitimos poder abrir sesion en multiples dispositivos.
         */

        if( usuario.getRol().equals("Admin") || usuario.getRol().equals("Moderator")){
            refreshTokenRepository.deleteRefreshTokenByUsuario(usuario);
            logger.info("Se elimaron los tokens de la base de datos asociados al usuario {} exitosamente", usuario);
        }

        //fecha de expiracion del refresh Token
        Date tokenExpiracion = jwtService.getExpiration(refreshToken);
        Instant expiracion = tokenExpiracion.toInstant();

        logger.debug("Fecha de expiración extraída del token: {}", expiracion.atZone(ZoneId.of("UTC")));

        //Mapeamos a la entidad refreshToken para persistir en la base de datos
         RefreshToken refreshTokenEntity = new RefreshToken();
         refreshTokenEntity.setToken(refreshToken);
         refreshTokenEntity.setRevoked(false);
         refreshTokenEntity.setUsuario(usuario);
         refreshTokenEntity.setFechaExpiracion(expiracion);
        logger.info("Fecha de expiracion del token de refresh: {}", refreshTokenEntity.getFechaExpiracion());


        refreshTokenRepository.save(refreshTokenEntity);
         logger.info("Mapeo a entidad RefreshToken exitoso: {}", refreshToken);
        logger.info("Mapeo a entidad RefreshToken exitoso: {}, expiración guardada: {}", refreshToken, refreshTokenEntity.getFechaExpiracion().atZone(ZoneId.of("UTC")));

         return  new AuthResponse(jwtToken,refreshToken);
    }

    /**
     * Registra un nuevo usuario, codifica su contraseña y genera los tokens JWT.
     * @param request DTO con los datos del usuario a registrar.
     * @return AuthResponse con accessToken y refreshToken.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request){
        logger.debug("Iniciando autenticacion para el usuario (register)");

        /*Hacemos una consulta a la base de datos, y verificamos si el correo del usuario esta presente,
        si es asi, mandamos una excepcion de que ya existe una cuenta con ese correo, si no seguimos con el proceso
        de registrarlo
         */

        if( usuarioRepository.findByEmail(request.email()).isPresent()){

            // Envolvemos el email único en una lista
            List<String> emailDuplicado = Collections.singletonList(request.email());
            logger.warn("Intento de registro con email duplicado: {}",emailDuplicado);

            throw new EntidadDuplicadaException("Email ya esta asociado a una cuenta", "email", emailDuplicado);
        }


        //Convertimos el RegisterRequest a un Usuario.
        Usuario usuario = usuarioRegisterMapper.toEntity(request, this.passwordEncoder);
        usuarioRepository.save(usuario);
        logger.info("Usuario registrado exitoxamente: {}", usuario);

        //Creamos el perfil en el contexto de Spring Security
        UserDetails userDetails= new CustomUserDetails(usuario);

        //Creamos tokens para el usuario
        String jwtToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        //Mapeamos a la entidad refreshToken para persistir en la base de datos
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setRevoked(false);
        refreshTokenEntity.setUsuario(usuario);
        refreshTokenEntity.setFechaExpiracion(Instant.now().plusMillis(jwtService.getEXPIRATION_REFRESH_TOKEN()));

        refreshTokenRepository.save(refreshTokenEntity);
        logger.info("refresh token persistido correctamente {}",refreshTokenEntity);
        return new AuthResponse(jwtToken,refreshToken);

    }

    /**
     * Renueva el accessToken a partir de un refreshToken válido.
     * También implementa rotación de refresh tokens y manejo de lista negra.
     * @param oldRefreshToken Refresh token existente.
     * @return AuthResponse con nuevo accessToken y refreshToken.
     */
    @Transactional
    public AuthResponse refreshToken(String oldRefreshToken){
        logger.debug("Inciando el proceso de creacion de rotacion de tokens y lista Redis ");

        // Verificamos si el token que nos mandan, esta presente en la base de datos; si no lo esta directamente mandamos una excepcion
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByToken(oldRefreshToken)
                .orElseThrow(() -> new EntidadNoEncontradaException("El refresh Token no ha sido encontrado"));

        //Si esta presente validamos el refresh token de dos maneras : Verificamos si ya ha sido revocado o ha expirado naturalmente

        if( refreshToken.getRevoked() || refreshToken.getFechaExpiracion().isBefore(Instant.now())){
            //El token que nos pasan ya ha sido revocado (por logout o por rotacion de token)
            //o su fecha de expriacion paso

            throw  new JwtRevokedException( "Refresh Token invalido o expirado");
        }

        //Obtenemos el username (email) del refresh token que nos mandan
        String username = jwtService.getUsernameFromToken(oldRefreshToken);

        //Con el username obtenido, cargamos los datos del usuario (en el contexto de Spring Security0
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Obtenemos al usuario
        Usuario usuario = ((CustomUserDetails) userDetails).getUsuario();

        // --- 4. ROTACIÓN CENTRAL: Invalidar el ANTIGUO y generar uno NUEVO ---

        //A. Marcamos el refresh token que acabamos de usar como REVOCADO en la DB
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        //B. Añadimos el Refresh token antiguo a la lista negra de Redis, nadie podra usar ese token para estar autenticado
        tokenBlackListService.blackListToken(oldRefreshToken, jwtService.getRemainingTime(oldRefreshToken));

        //C. Generamos un nuevo par de tokens
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        //D. Guardamos el nuevo refresh token en la base de datos (este es el nuevo token que el usuario usara para las renovaciones)
        //Mapeamos a la entidad refreshToken para persistir en la base de datos
        RefreshToken newRefreshTokenEntity = new RefreshToken();
        newRefreshTokenEntity .setToken(newAccessToken);
        newRefreshTokenEntity .setRevoked(false);
        newRefreshTokenEntity .setUsuario(usuario);
        newRefreshTokenEntity .setFechaExpiracion(Instant.now().plusMillis(jwtService.getEXPIRATION_REFRESH_TOKEN()));

        refreshTokenRepository.save(newRefreshTokenEntity);

        return new AuthResponse(newAccessToken,newRefreshToken);
    }

    /**
     * Realiza logout del usuario, revocando accessToken y refreshToken.
     * @param accessToken Token de acceso a invalidar.
     * @param refreshToken Refresh token a revocar.
     */
    @Transactional
    public void logout( String accessToken, String refreshToken){

        //1. Invalidar el accessToken actual en redis(TTL = tiempo restante)
        tokenBlackListService.blackListToken(accessToken, jwtService.getRemainingTime(accessToken));
        //2. invalidar el refreshToken en la base de datos y en Redis
        RefreshToken refreshTokenEntity = refreshTokenRepository.findRefreshTokenByToken(refreshToken)
                .orElseThrow(()-> new EntidadNoEncontradaException("El token no ha sido encontrado"));
        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);

        tokenBlackListService.blackListToken(refreshToken, jwtService.getRemainingTime(refreshToken));

        SecurityContextHolder.clearContext();
    }
}
