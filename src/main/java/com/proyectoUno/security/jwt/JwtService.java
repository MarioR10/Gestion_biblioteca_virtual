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

        // Inicia la construcción de un JWT mediante el patrón Builder. Utilizamos el objeto JwtBuilder para construir un JWT (String) paso a paso.
        return Jwts.builder()

                // Establece el "subject" del token.
                // Normalmente es el identificador del usuario (username, email, id).
                .subject(userDetails.getUsername())

                // Fecha en la que el token fue creado.
                // Sirve como referencia temporal del token.
                .issuedAt(new Date(System.currentTimeMillis()))

                //Establece un claim personalizado, le da el tipo de token.
                .claim("type", "ACCESS_TOKEN")

                // Fecha en la que el token deja de ser válido.
                // Se calcula a partir del tiempo actual + duración configurada.
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_ACCESS_TOKEN))

                /*
                 * Firma el token.
                 *
                 * - Usa la clave secreta devuelta por getSigningKey()
                 * - La firma se realiza usando  HMAC (algoritmo simétrico)
                 * - El algoritmo HS256 / HS384 / HS512 se elige según el tamaño de la clave
                 *
                 * Esta firma:
                 * - Garantiza que el token no fue modificado
                 * - Permite verificar que fue emitido por este servidor
                 */
                 .signWith(getSigningKey())

                /*
                 * Finaliza la construcción del JWT (Este metodo es el equivalente a 'build()'):
                 * - Genera header, payload y firma
                 * - Los codifica en Base64URL
                 * - Los une en el formato compacto: header.payload.signature
                 */.compact();

    }

    /**
     * Genera un refresh token para obtener nuevos tokens de acceso sin re-login.
     *
     * @param userDetails Datos del usuario autenticado.
     * @return Refresh token firmado.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails
                .getUsername())
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
                username.equals(userDetails.getUsername()) && !isTokenExpired(token);
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
                        (claims -> claims.getSubject()));
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
     * Metodo para obtener todos los claims del token
     * REGLA: NUNCA SE LEE UN CLAIM SIN VALIDAR ANTES LA FIRMA DE UN TOKEN.
     * Este flujo se hace asi porque no se puede confiar en el contenido de un JWT sin primero verificar la firma secreta
     *
     * este método:
     * 1. Toma un token, valida su firma con la clave secreta
     * 2. devuelve el contenido (TODOS LOS CLAIMS)
     *
     */
    private Claims getAllClaims(String token) {

        // Patrón de diseño Builder:
        // Usamos un Builder para crear paso a paso un JwtParser.
        return Jwts.parser()

                /*
                 * Asignamos la clave secreta con la que se validará la firma.
                 *
                 * Esta debe ser la misma clave usada para firmar el token.
                 * Si no coincide, la validación falla.
                 */
                .verifyWith(getSigningKey())

                // Finaliza la configuración y construye una instancia inmutable de JwtParser.
                .build()

                /*
                 * Usa el JwtParser para:
                 * 1. Validar la firma del token con la clave secreta (lanza excepción si falla)
                 * 2. Separar y decodificar las partes del JWT (header, payload y firma)
                 * 3. Convertir el contenido en un objeto Jwt<Claims> que Java entiende
                 */
                .parseSignedClaims(token)

                // Devuelve el objeto Claims extraído del payload del token. (  obtiene todos los claims)
                .getPayload();
    }

    /**
     * Metodo que permite extraer un claim especifico.
     * Utiliza una función (claimsResolver) para determinar qué claim específico extraer.
     *
     * @param token          El JWT del cual se extraerá el claim.
     * @param claimsResolver Una función que toma un objeto tipo Claims y devuelve el valor del claim deseado tipo T. (interfaz funcional)
     * @return El valor del claim extraído.
     */

    public <T> T getClaim(
            //le pasamos el token del cual extraera el claim
            String token,

            /*
             * Función que define qué dato se quiere obtener del objeto Claims.
             * El lambda que se pasa aquí es una implementación de Function<Claims, T>
             * y contiene la lógica para extraer un claim específico
             * (por ejemplo: subject, expiration, o un claim personalizado).
             */
            Function<Claims, T> claimsResolver) {

        // 1. Obtenemos todos los claims del token (aqui recordemos que va la validación del token), el resultado es un objeto 'Claims' con toda la información.
        final Claims claims = getAllClaims(token);

        return

                /*
                 * 2. Se ejecuta la lógica recibida (lambda) sobre el objeto Claims.
                 *
                 * El método apply(...) pertenece a la interfaz Function
                 * y aplica la lógica definida en el lambda para devolver el dato deseado.
                 */
                claimsResolver.apply(claims);

    }

    /**
     * Obtiene la clave secreta que se usará para firmar y verificar JWT.
     * <p>
     * Esta aplicación usa JWT firmados con HMAC.
     * HMAC necesita una clave secreta compartida (la misma para firmar y validar).
     * <p>
     * La clave se guarda como texto en Base64 en application.properties,
     * pero para usarla en criptografía debe convertirse a bytes
     * y luego a un objeto SecretKey.
     */
    private SecretKey getSigningKey() {

        // La clave (SECRET_KEY) viene como String en Base64.
        // Aquí la decodificamos para obtener los bytes reales de la clave.

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        /*
         * Construye un SecretKey a partir de bytes.
         *
         * El SecretKey:
         * - Contiene el material secreto (bytes)
         * - Indica que esta clave es válida para algoritmos HMAC
         *
         * No ejecuta HMAC ni firma nada.
         * Solo describe la clave que HMAC utilizará más adelante.
         */
        return Keys.hmacShaKeyFor(keyBytes); // Devuelve el objeto SecretKey
    }


    /**
     * Obtiene el tipo del token
     * @return
     */
    public  String getTokenType(String token){

        return  getClaim(
                token,

                /*Los claims personalizados no tienen método propio (como:
                getSubject() → sub
                getExpiration() → exp

                Por eso se obtienen como un Map.Se busca por su llave (nombre) y se obtiene su valor.
                pasanmos nombre del claim y tipo esperado <--Devuelve el valor asociado
                 */

                claim -> claim.get("type", String.class));
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
