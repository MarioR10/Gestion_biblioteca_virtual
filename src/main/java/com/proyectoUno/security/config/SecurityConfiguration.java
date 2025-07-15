package com.proyectoUno.security.config;


// Configuración global de seguridad

import com.proyectoUno.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuracion donde definimos la cadena de filtros que debe de seguir la solicitud Http,
 * la clase se encarga de decirle a Spring security que rutas son publicas, requieren autenticacion , cuales requieren ciertos roles,
 * reglas de proteccion.
 * Esta clase es importante porque le indicamos:
 * 1. Las rutas publicas
 * 2. Las rutas que se necesitan autenticacion
 * 3. como se manejan las seciones ( o la ausencia de ellas)
 * 4. Filtros personalizados a ustilizar
 * 5.Como se autentican los usuarios
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf-> csrf.disable())  // Deshabilita la protección CSRF (comúnmente para APIs REST sin sesiones)
                .authorizeHttpRequests( auth -> auth

                //Acceso publico a las rutas de autentificacion (Register, Login), no se necesita estar autentificado
                                .requestMatchers("/auth/**").permitAll()
                //Cualquier otra ruta requiere autentificacion del usuario
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                 //Configura la politica de creacion de sesiones como STATELESS,
                 // ya que usaremos JWT y no necesitamos sesiones HTTP para el estado de autenticación
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider) // Establece el proveedor de autentificacion personalizado
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Añade nuestro filtro JWT
                                                                                                       // antes del filtro de autenticación por usuario/contraseña de Spring

        return http.build(); // Construye y devuelve el SecurityFilterChain configurado
    }


}
