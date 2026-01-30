package com.proyectoUno.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

/**
 * Servicio responsable de todas las operaciones relacionadas con JSON Web Tokens (JWT).
 * Su objetivo es:
 * 1. Generar tokens de acceso y refresh para usuarios autenticados.
 * 2. Validar tokens recibidos en las peticiones.
 * 3. Extraer información (claims) de los tokens.
 */

@Service
public class JwtService {

    /**
     * Clave secreta para firmar y verificar tokens.
     * Inyectada desde 'application.properties' como 'jwt.secret'.
     * Garantiza la integridad y autenticidad del token.
     */
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Tiempo de expiración del token de acceso en milisegundos.
     */
    @Value("${jwt.expiration}")
    private Long EXPIRATION_ACCESS_TOKEN;

    /**
     * Tiempo de expiración del refresh token en milisegundos.
     */
    @Value("${jwt.refresh-expiration}")
    private Long EXPIRATION_REFRESH_TOKEN;

    // ======================= MÉTODOS PRINCIPALES =======================//

    /**
     * Genera un token de acceso para un usuario.
     *
     * @param userDetails Datos del usuario autenticado.
     * @return JWT firmado como String.
     */
    public String generateAccessToken(UserDetails userDetails) {

        // Patrón de diseño Builder: Utilizamos el objeto JwtBuilder para construir un JWT (String) paso a paso.
        return
                Jwts.builder() //Inicia la construcción de un nuevo JWT, obteniendo una instancia de JwtBuilder.
                        //Este builder permite configurar las diferentes partes del token.

                        // Los "claims" son la información (payload) contenida en el token.
                        .subject(userDetails.getUsername()) //'sub' (Subject): Establece el identificador principal del token,
                        .issuedAt(new Date(System.currentTimeMillis())) //'iat' (Issued At): Establece la fecha y hora de creación del token. (toma la actual)
                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_ACCESS_TOKEN)) // 'exp' (Expiration): Establece la fecha y hora de expiración del token.
                        //toma la actual y le suma lo que establecimos y crea la fecha.
                        .signWith(getSigningKey()) // Firma el token. Se especifica la clave secreta (getSigningKey())
                        //el algoritmo de firma (HS256) a utilizar.
                        //Esta firma garantiza la integridad y autenticidad del token.
                        .compact(); //Finaliza la construcción del JWT. Este metodo es el equivalente a 'build()'.
        //Toma toda la configuración ensambla el JWT en su formato compacto (Header.Payload.Signature)
        // y lo serializa a una cadena de texto (String) lista para ser utilizada.
    }

    /**
     * Genera un refresh token para obtener nuevos tokens de acceso sin re-login.
     *
     * @param userDetails Datos del usuario autenticado.
     * @return Refresh token firmado.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return
                Jwts.builder()
                        .subject(userDetails.getUsername())
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_REFRESH_TOKEN))
                        .claim("type", "REFRESH_TOKEN")
                        .signWith(getSigningKey())
                        .compact();
    }

    /**
     * Valida si un JWT es válido para un usuario determinado.
     * Un token se considera válido si:
     * 1. El identificador del usuario en el token, coincide con el identificador del UserDetails.
     * 2. El token no ha expirado.
     *
     * @param token       El token JWT a validar.
     * @param userDetails Los detalles del usuario contra el que se valida el token.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        //Guardamos el username (identificador) que viene en el token
        final String username = getUsernameFromToken(token);

        return
                // Comprueba que el username coincida con el de userDetails y que el token no haya expirado.
                (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // --- MÉTODOS PÚBLICOS AUXILIARES PARA EXTRAER INFORMACIÓN ---

    /**
     * Extrae el username (el "subject" del token) de un JWT.
     *
     * @param token El JWT del cual se extraerá el username.
     * @return El username contenido en el token.
     */
    public String getUsernameFromToken(String token) {
        return
                // Usa el metodo genérico getClaim para extraer específicamente el 'subject' del token.
                getClaim(
                        //pasamos el token como parametro, donde buscara el claim
                        token,
                        //Expresion lambda,forma abreviada de Function<Claims,String>, a indica que se transforma
                        // el tipo Claims a tipo String utilizando el metodo getSubject() del objeto claims
                        (claims -> claims.getSubject())
                );
    }

    // ======================= MÉTODOS AUXILIARES =======================//

    /**
     * Verifica si un token ha expirado comparando su fecha de expiración con la fecha actual.
     *
     * @param token El token a verificar.
     * @return {@code true} si el token ha expirado, {@code false} si todavía es válido.
     */
    private boolean isTokenExpired(String token) {
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
    public Date getExpiration(String token) {
        return getClaim(
                //pasamos el token como parametro, donde buscara el claim
                token,
                //Expresion lambda,forma abreviada de Function<Claims,Date>, indica que se transforma el tipo
                //Claims al tipo Date utilizando el metodo getExpiration() del objeto claims
                (claims -> claims.getExpiration()));
    }

    /**
     * Metodo genérico para extraer cualquier claim de un token.
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
            Function<Claims, T> claimsResolver) {

        // Primero, obtiene todos los claims del token.
        // El resultado es un objeto 'Claims' con toda la información.
        final Claims claims = getAllClaims(token);

        return
                // Ejecuta la lógica definida en 'claimsResolver', pasándole el objeto 'Claims' completo, medainte el metodo apply
                // La lambda procesa este 'Claims' y devuelve el dato específico de tipo 'T' que se buscaba.
                claimsResolver.apply(claims);
    }

    /**
     * Metodo que toma un token, valida su firma con la clave secreta y devuelve el contenido (claims)
     * Este flujo se hace asi porque no se puede confiar en el contenido de un JWT sin primero verificar la firma secreta
     */
    private Claims getAllClaims(String token) {

        // Patrón de diseño Builder: Ocupamos un objeto Builder para crear paso a paso un objeto JwtParser.
        return
                Jwts.parser()  // Inicia la construcción de un JwtParserBuilder para configurar la validación y extracción del token.
                        // Nota: Jwts.parser() devuelve un builder que permite encadenar configuraciones y luego crear un objeto JwtParser.
                        .verifyWith(getSigningKey()) // Asigna la clave secreta que se usará para verificar la firma del token.
                        // La firma del JWT se valida contra esta clave.
                        .build() // Finaliza la configuración y construye una instancia inmutable de JwtParser.
                        // El JwtParser ya está listo para procesar tokens. (este objeto se crea para poder leer, dividir en partes,
                        // decodificarlo, validar, convertitlo en un objeto que java entiend. Brinda herramientas y funcionalidades para esto
                        .parseSignedClaims(token)  // Usa el JwtParser para:
                        // 1. Validar la firma del token con la clave secreta (lanzará excepción si falla).
                        // 2. Separar y decodificar las partes del JWT (header, payload, firma). Esto nos devuelve un
                        // Jwt<Claims> que java entiende
                        .getPayload(); // Devuelve el objeto Claims extraído del payload del token.
    }

    /**
     * Decodifica la clave secreta desde formato base64 y la convierte en un objeto {@link SecretKey}
     * apto para ser usado con el algoritmo de firma HMAC-SHA256.
     *
     * @return La clave de firma lista para usar con el método signWith.
     */
    private SecretKey getSigningKey() {

        // Decodificamos la clave secreta desde base64 a una matriz de bytes
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        /* Preparamos la clave secreta decodificada para ser usada con el algoritmo HMAC-SHA256,
           creando un SecretKey que se pasará al metodo signWith para firmar el token. */
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ======================= MÉTODOS DE UTILIDAD =======================//


    public Long getEXPIRATION_REFRESH_TOKEN() {
        return EXPIRATION_REFRESH_TOKEN;
    }

    public Long getEXPIRATION_ACCESS_TOKEN() {
        return EXPIRATION_ACCESS_TOKEN;
    }

    /**
     * Calcula la duración restante hasta la expiración del token.
     */
    public Duration getRemainingTime(String token) {

        // obtenemos la fecha de expiracion del token
        Date expirationDate = getExpiration(token);

        //La convertimos a Instant
        Instant expirationInstant = expirationDate.toInstant();

        //Calculamos la duracion entre el momento actual y la expiracion

        return Duration.between(Instant.now(), expirationInstant);
    }
}
