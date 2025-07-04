package com.proyectoUno.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Clase responsable de todas las operaciones relacionadas con JSON Web Tokens (JWT).
 * Sus responsabilidades principales son:
 * 1. Generar tokens para usuarios autenticados.
 * 2. Validar los tokens recibidos en las peticiones.
 * 3. Extraer información (claims) de los tokens.
 */

@Service
public class JwtService {

    /**
     * La clave secreta utilizada para firmar y verificar los tokens JWT.
     * Esta es una medida de seguridad  para asegurar que los tokens no sean alterados.
     * La anotación @Value inyecta el valor desde 'application.properties',
     * buscando la propiedad llamada "jwt.secret" que contiene el valor de la llave secreta.
     */
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Tiempo de expiracion del JWT
     */
    @Value("${jwt.expiration}")
    private Long EXPIRATION_TOKEN;

    // --- MÉTODOS PÚBLICOS PRINCIPALES ---


    /**
     * Genera un nuevo token JWT para un usuario específico.
     * @param userDetails Los detalles del usuario (proporcionados por Spring Security) para quien se genera el token.
     * @return Un String que representa el JWT compacto y firmado.
     */
    public String generateToken(UserDetails userDetails){

        return
                Jwts.builder() // Inicia la construcción de un nuevo JWT.
                 // Los "claims" son la información (payload) contenida en el token.  Claims estándar:

                .setSubject(userDetails.getUsername()) // 'sub' (Subject): Identificador del usuario (username)
                .setIssuedAt( new Date(System.currentTimeMillis())) // 'iat' (Issued At): Fecha de creación del token.
                .setExpiration( new Date( System.currentTimeMillis() + EXPIRATION_TOKEN)) // 'exp' (Expiration): Fecha de expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) //Firma el toke. se especifica que llave secreta  y que algoritmo utilizar
                .compact(); //Finaliza la construcción y serializa el token a su formato String (header.payload.signature).
    }

    /**
     * Valida si un token JWT es válido para un usuario determinado.
     * Un token se considera válido si:
     * 1. El identificador del usuario en el token, coincide con el identificador del UserDetails.
     * 2. El token no ha expirado.
     * @param token       El token JWT a validar.
     * @param userDetails Los detalles del usuario contra el que se valida el token.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails){

        //Guardamos el username (identificador) que viene en el token
        final String username= getUsernameFromToken(token);

        return
                // Comprueba que el username coincida con el de userDetails y que el token no haya expirado.
                (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // --- MÉTODOS PÚBLICOS AUXILIARES PARA EXTRAER INFORMACIÓN ---

    /**
     * Extrae el username (el "subject" del token) de un JWT.
     * @param token El JWT del cual se extraerá el username.
     * @return El username contenido en el token.
     */
    public  String getUsernameFromToken(String token){

        return
                // Usa el metodo genérico getClaim para obtener específicamente el "subject".
                getClaim(
                        //pasamos el token como parametro, donde buscara el claim
                        token,
                        //Expresion lambda,forma abreviada de Function<Claims,String>, esta lambda indica que se transforma
                        // el tipo Claims al tipo String del claim especifico Subject
                        (claims -> claims.getSubject())
                );
    }

    // --- MÉTODOS PRIVADOS Y PROTEGIDOS DE AYUDA ---


    /**
     * Verifica si un token ha expirado comparando su fecha de expiración con la fecha actual.
     *
     * @param token El token a verificar.
     * @return {@code true} si el token ha expirado, {@code false} si todavía es válido.
     */
    private boolean isTokenExpired(String token){

        return
                // Obtiene la fecha de expiración y comprueba si es anterior a la fecha actual.
                getExpiration(token).before(new Date());
    }

    /**
     * Obtiene la fecha de expiración de un token.
     *
     * @param token El token del cual obtener la fecha.
     * @return La fecha de expiración (Date).
     */
    public Date getExpiration(String token){

        return getClaim(
                //pasamos el token como parametro, donde buscara el claim
                token,
                //Expresion lambda,forma abreviada de Function<Claims,Date>, esta lambda indica que se transforma el tipo
                //Claims al tipo Date del clains especifico Expiration
                (claims -> claims.getExpiration()));

    }

    /**
     * Método genérico para extraer cualquier claim de un token.
     * Utiliza una función (claimsResolver) para determinar qué claim específico extraer.
     *
     * @param token          El JWT del cual se extraerá el claim.
     * @param claimsResolver Una función que toma un objeto tipo Claims y devuelve el valor del claim deseado tipo T. (interfaz funcional)
     * @return El valor del claim extraído.
     */

    public <T> T getClaim(
            //le pasamos el token del cual extraera el claim
            String token,
            //interfaz que convierte un tipo Claim a un tipo T (dependiendo del claim); la forma compacta de escribirlo es
            // mediante una funcion lambda
            Function<Claims,T> claimsResolver){

        // Primero, obtiene todos los claims del token.
        final Claims claims = getAllClaims(token);

        return
                // Luego, aplica la función resolver para obtener y devolver el claim específico.
                claimsResolver.apply(claims);
    }

    /**
     * Parsea el token JWT para extraer todo su cuerpo (payload), que contiene todos los claims.
     * Este método verifica la firma del token usando la clave secreta al parsearlo.
     * Si la firma es inválida, lanzará una excepción.
     *
     * @param token El JWT a parsear.
     * @return Un objeto {@link Claims} que contiene todos los datos del payload del token.
     */
    private Claims getAllClaims(String token){

        return Jwts
                .parser()
                .setSigningKey(getSigningKey()) // Establece la clave para verificar la firma.
                .build()
                .parseClaimsJws(token) // Parsea y valida el token.
                .getBody(); // Devuelve el cuerpo (payload) del token.
    }

    /**
     * Procesa la clave secreta (que está en formato String y codificada en Base64)
     * y la convierte en un objeto {@link Key} apto para ser usado en algoritmos de firma HMAC-SHA.
     *
     * @return La clave de firma lista para usar.
     */
    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}
