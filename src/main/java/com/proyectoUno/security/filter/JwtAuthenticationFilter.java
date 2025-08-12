package com.proyectoUno.security.filter;

import com.proyectoUno.security.exception.JwtRevokedException;
import com.proyectoUno.security.jwt.JwtService;
import com.proyectoUno.security.service.TokenBlackListService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.ServletException;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 *  Esta clase es un filtro  en el contexto de Spring SecurityTiene, tiene como proposito interceptar las solicitudes
 *  Http entrantes a la aplicacion,para verificar si la solicitud trae consigo un JWT valido  y,si lo hace,autentifica
 * al usuario.Esto ocurre antes que la solicitud llegue  a los controladores.
 *Al extender la clase  OncePerRequestFilter asegura que dicho proceso de validacion y autenticacion ocurra solo una vez por
 * solicitud
 */
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlackListService tokenBlackListService;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenBlackListService tokenBlackListService){

        this.jwtService=jwtService;
        this.userDetailsService=userDetailsService;
        this.tokenBlackListService = tokenBlackListService;
    }

    //Metodo principal, Spring Security lo llama automaticamente para cada solicitud HTTP. Aqui se define la logica
    //personalizada del filtro.
    @Override
    protected  void doFilterInternal(

            //Representa la solicitud HTTP entrante completa (contiene  encabezado, cuerpo, URl, etc)
            @NotNull HttpServletRequest request,
            //Representa la respuesta HTTP que se le enviara al cliente
            @NotNull HttpServletResponse response,
            //Representa la cadena de filtros de Spring Security, luego que este filtro haga su trabajo pasa la solciitud
            //al siguiente filtro en la cadena
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        //1. definimos variables importantes

        //Almacena el encabezado de la solicitud HTTP llamado "Authorization" ya que ahi suelen venir los JWT
        //por ejemplo  Authorization: Bearer <el_jwt>
        final String authHeader = request.getHeader("Authorization");
        //Almacena el JWT
        final String jwt;
        //Almacena el UserName
        final String username;

        /*
        2. Se lleva acabo dos verificaciones:
            2.1  Verifica si el encabezado no esta presente
            2.2  Verifica si el encabezado  existe pero no comienza con "Bearer" (que es el formato cuando ocupamos JWT)

         Los encabezados de una solicitud Http son pares clave-valor (Authorization: Bearer <token>, u otro esquema como Basic
          en lugar de Bearer), nos interesa manejar autenticación basada en JWT */

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //Si alguna de las condiciones anteriores se cumple, signica que no hay un JWT que deba procesar,
            //pasa la solicitud al siguiente filtro(si lo hay) o directamente al controlador
            filterChain.doFilter(request, response);
            return; //detiene la eejcucion de este filtro, evitando que se proceda con la ejecucion
        }

        //3. Si el encabezado existe y comienza con el prefijo "Bearer", extraemos la cadena JWT.
        //substring(7) omite los primeros 7 caracteres "Bearer "  empieza directamente en el JWT indice (7) de la cadena
        jwt = authHeader.substring(7); // "Bearrer " tiene 7 caracteres

        try {
            // --- VERIFICAR SI EL TOKEN ESTÁ EN LA LISTA NEGRA DE REDIS ---

            if (tokenBlackListService.isTokenBlackListed(jwt)) {
                logger.info("Token marcado como revocado");
                throw new JwtRevokedException("El token fue revocado o invalido");

            }
            //4. Extrae el username del JWT
            username = jwtService.getUsernameFromToken(jwt);

            //5. Aca verificamos dos cosas
            //5.1 Que se haya extraido exitosamente el subject del JWT
            /*5.2 Comprueba si no existe una autenticacion para la solicitud actual en el contexto de Spring Security
                SecurityContextHolder es una clase donde se almacena la informacion del usuario autentificado (un Authetication),
                devuelve un  objeto Authetication si esta autentificado o un null si no lo esta. Si  no es null significa
                que otro mecanismo o filtro ya autentifico al usuario para esta solicitud y no es necesario volver a hacerlo*/

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //6. Carga los detalles del usuario desde la base de datos
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                //7.Verifica si el token es válido (no expirado, firma correcta, etc.) para este usuario cargado
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    //8. Si el token es valido, crea un objeto de autentificacion (representa un usuario autentificado)
                    // UsernamePasswordAuthenticationToken es un objeto Authentication que indica que el usuario está autenticado
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, //detalles del usuario
                            null,  // La contraseña (ya no es necesaria porque ya se autenticó con el token)
                            userDetails.getAuthorities()
                    );

                    // 9. Establecer detalles de autenticación (opcional pero buena práctica)
                    // Esto añade detalles como la dirección IP y la sesión del usuario al objeto de autenticación
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 10. Actualizar el contexto de seguridad de Spring
                    // ¡Esto es lo más importante! Le dice a Spring Security que este usuario está autenticado
                    // para la solicitud actual. Ahora, Spring Security permitirá el acceso a recursos protegidos.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // 11. Pasar la solicitud al siguiente filtro en la cadena de seguridad
            // Continúa con la ejecución de los demás filtros de Spring Security y, finalmente, al controlador
            filterChain.doFilter(request, response);

        }catch (JwtRevokedException | JwtException e) {
            request.setAttribute("excepcion", e);
            logger.error("Error de autenticación JWT: {}", e.getMessage());
            filterChain.doFilter(request, response);
        } catch (Exception e){
            request.setAttribute("excepcion", e);
            filterChain.doFilter(request, response);
        }
    }
}
