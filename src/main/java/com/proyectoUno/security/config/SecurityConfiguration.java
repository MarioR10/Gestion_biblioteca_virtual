package com.proyectoUno.security.config;


// Configuración global de seguridad

import com.proyectoUno.security.exception.CustomAccessDeniedHandler;
import com.proyectoUno.security.exception.CustomAuthenticationEntryPoint;
import com.proyectoUno.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración donde definimos la cadena de filtros de seguridad
 * que debe seguir cada solicitud HTTP.
 * Su objetivo es indicarle a Spring Security qué rutas son públicas,
 * cuáles requieren autenticación, qué roles son necesarios en cada caso,
 * cómo se gestionan las sesiones y qué filtros personalizados deben ejecutarse.
 * En términos generales, aquí definimos:
 * 1. Las rutas públicas (sin necesidad de autenticación).
 * 2. Las rutas privadas que requieren autenticación.
 * 3. Los roles necesarios para acceder a determinadas rutas.
 * 4. La política de sesiones (en este caso, sin sesiones porque usamos JWT).
 * 5. Los filtros personalizados que deben añadirse a la cadena de seguridad.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar anotaciones como @PreAuthorize en los controladores o servicios
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Configura la cadena de filtros de Spring Security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                // 1. Deshabilitamos CSRF ya que nuestra API es stateless y usa JWT
                .csrf(csrf-> csrf.disable())

                // 2. Definimos cómo manejar errores de autenticación y autorización
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))

                // 3. Definimos las reglas de autorización para las rutas
                .authorizeHttpRequests( auth -> auth

                                // --- Rutas públicas (no requieren autenticación) ---
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/libro/**").permitAll()
                                .requestMatchers("/error").permitAll()

                        // --- Rutas privadas (requieren autenticación + rol específico) ---
                                .requestMatchers(HttpMethod.DELETE, "/api/libro/**").hasRole("Admin")
                                .requestMatchers(HttpMethod.PUT, "/api/libro/**").hasRole("Admin")
                                .requestMatchers(HttpMethod.POST, "/api/libro/**").hasRole("Admin")

                                .requestMatchers("/api/prestamo/**").hasRole("Admin")
                                .requestMatchers("/api/usuario/**").hasRole("Admin")

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                // 4. Configuración de sesiones: como usamos JWT, definimos STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 5. Registramos nuestro AuthenticationProvider
                .authenticationProvider(authenticationProvider)

                // 6. Insertamos el filtro JWT antes del filtro estándar de login con usuario/contraseña
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Construimos y devolvemos el SecurityFilterChain configurado
        return http.build();
    }


}
