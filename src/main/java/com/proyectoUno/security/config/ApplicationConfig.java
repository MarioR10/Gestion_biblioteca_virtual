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
 * Clase de configuracion, define beans necesarios para la autentificacion y autorizacion de usuarios
 * Define multiples metodos, como estan anotados con @Bean le dicen a Spring que el valor de retorno debe
 * ser registrado como un bean en el contenedor de spring. Estos beans puedne ser inyectados en otras partes de la aplicacion
 */
@Configuration
public class ApplicationConfig {


    private  final UserDetailsService userDetailsService;

    @Autowired
    public ApplicationConfig( UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    
    @Bean
    public AuthenticationProvider authenticationProvider(){

        /*
        DaoAuthenticationProvider es una de muchas implementaciones de AuthenticationProvider, su funcion es autentificar
        al usuario con los datos obtenidos. verifica que la contraseña dada coinicida con la almacenada en la base de datos
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
        proceso de autentificacion.

       Esta clase es importante porque nos devuelve un  AuthenticationManager, al utilizar metodos como getAuthenticationManager()
       que crean/devuelven un objeto ya configurado.

    */

        /*
    NOTA: Configuración del AuthenticationManager: Cuando Spring Security construye el 'AuthenticationManager'
          (que obtenemos a través de 'configuration.getAuthenticationManager()'), internamente utiliza un
          'AuthenticationManagerBuilder'. Este 'builder' ESPECÍFICAMENTE ESCANEA el contenedor de Spring
          y descubre AUTOMÁTICAMENTE TODOS los beans de tipo 'AuthenticationProvider' que hemos definido.
          'DaoAuthenticationProvider' es uno de ellos, por eso sin especificarlo nosotros sabe a que provider delega las tareas.
          Por lo tanto, cuando luego llamas a 'authenticationManager.authenticate(token)',
          no  pasamos el 'Provider'. El 'AuthenticationManager' simplemente itera sobre su lista
          interna de 'AuthenticationProvider's, preguntando a cada uno si 'soporta' (puede manejar) el tipo de
          'Authentication' que le pasamos (ej. UsernamePasswordAuthenticationToken). Una vez que encuentra uno que
          lo soporta (tu DaoAuthenticationProvider), le DELEGA la tarea de autenticación real.

         */

        return  configuration.getAuthenticationManager();

    }

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
