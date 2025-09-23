package com.proyectoUno.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Configuración principal de seguridad para la aplicación.
 * Define beans clave para autenticación y autorización de usuarios:
 * - AuthenticationProvider: valida credenciales usando UserDetailsService.
 * - AuthenticationManager: coordina el proceso de autenticación.
 * - PasswordEncoder: codifica y verifica contraseñas de manera segura.
 */
@Configuration
public class ApplicationConfig {


    private  final UserDetailsService userDetailsService;

    @Autowired
    public ApplicationConfig( UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    /**
     * Bean que define cómo Spring Security autentica usuarios.
     * Utiliza DaoAuthenticationProvider, que compara username y password
     * proporcionados con los almacenados en la base de datos.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){

        /*
        DaoAuthenticationProvider es una de muchas implementaciones de AuthenticationProvider, su funcion es autentificar
        al usuario con los datos obtenidos. verifica que la contraseña y username dados coinicidan con los almacenados en
        la base de datos
         */
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        /*
        Aca le brindamos el perfil del usuario (en el contexto de spring security) para que pueda  cargar los datos,
        userDetailsService genera un CustomUserDetails que son los datos del usuario requerido por Spring Security
         */
        authProvider.setUserDetailsService(userDetailsService);

        /*
        Aca le brindamos una herramienta (un metodo) a DaoAuthenticationProvider() para codificar y validar la contraseña:
        compara la brindada con la almacenada
         */
        authProvider.setPasswordEncoder(passwordEncoder());

        /*
        retornamos el objeto configurado de tipo AuthenticationProvider (o DaoAuthenticationProvider)
        para que spring lo gestione como un Bean
         */
        return authProvider;
    }

    /**
     * Bean que provee el AuthenticationManager de Spring Security.
     * Es el coordinador de la autenticación: recibe credenciales,
     * delega la validación a los AuthenticationProviders y devuelve
     * un objeto Authentication con la información del usuario autenticado.
     * @param configuration Configuración proporcionada por Spring Security
     * @return AuthenticationManager ya configurado
     * @throws Exception Si ocurre un error al obtener el manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws  Exception{

        /*
    1. Primero, hay que saber que AuthenticationManager es el coordinador principal de la autenticación en Spring Security.
       Es una interfaz con implementaciones concretas como ProviderManager, que contienen la lógica real.
       Su trabajo es recibir las credenciales del usuario y delegar la autenticación a uno de los AuthenticationProviders configurados
       (como DaoAuthenticationProvider en nuestro caso).
       Si la autenticación es exitosa, AuthenticationManager devuelve un objeto Authentication (creado por el Provider) con la información del usuario autenticado,
       que luego se almacena en el contexto de seguridad para usarse durante toda la sesión.
    */
        /*
    2. Spring tiene centralizada la configuracion para el proceso de autentificacion, hace todo el trabajo por nosotros
        para que ocupemos metodos con la logica ya definida. AuthenticationConfiguration es una clase concreta que utiliza
        Spring Security para esto. Tiene la logica necesaria y los datos necesarios para el buen funcionamiento del
        proceso de autentificacion.Esta clase es importante porque nos devuelve un  AuthenticationManager, al utilizar metodos como getAuthenticationManager()
       que crean/devuelven un objeto ya configurado.
    */
        return  configuration.getAuthenticationManager();
    }

    /**
     * Bean que define cómo se codifican y verifican las contraseñas.
     * BCryptPasswordEncoder implementa PasswordEncoder y aplica un hash seguro
     * para almacenar contraseñas, garantizando que no se guarden en texto plano.
     * @return PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder(){

        /*
        PassswordEncoder es una interfaz fundamental en Spring Security, ya que es el contraro para la codficacion y la
        verificacion de la contraseña, clases como  BCryptPasswordEncoder que implementan esta interfaz, tienen metodos definidos
        para realizar estas tareas.

        La tarea es tomar la contraseña en texto plano, codificarla y luevo compararla con la almacenada en la base de datos.
         */
        return new BCryptPasswordEncoder();
    }

    }
