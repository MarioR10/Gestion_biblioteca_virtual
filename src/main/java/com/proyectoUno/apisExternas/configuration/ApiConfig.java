package com.proyectoUno.apisExternas.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class ApiConfig {

    /**
     *  Este metodo se utiliza para crear intancias de la clase WebClient, que nos ayuda a consultar APIs externas
     *  Utilizamos esta clase porque es la forma de moderna y recomendada por Spring para consumir APIs en Spring Boot.
     *  Utilizamos el patron de diseño Builder para construirlo.
     * @param builder
     * @param url
     * @return
     */
    @Bean
    WebClient webClient(
            //Aqui ocupamos el builder, el que nos ayuda a construir el objeto WebClient (Spring lo inyecta como dependencia)
            WebClient.Builder builder,

            //Con esto inyectamos valores desde nuestro archivo de propiedades
            @Value("${google.books.api.url}") String url){




        //Construimos el objeo WebClient y lo retornamos
        return
                // Construimos un WebClient personalizado usando un builder
                builder
                        // Establece la URL base, que se usará como raíz para todas las solicitudes
                        .baseUrl(url)
                        // Configura un conector HTTP personalizado para manejar conexiones y timeouts.
                        .clientConnector(

                                /* Un conector HTTP es el que se encarga de de enviar solicitudes y recibir solicitudes
                                 HTTP. Si no configuramos uno, Spring utiliza el predeterminado. Se utiliza uno
                                 personalizaado para poder configurar diversas cosas como: timeout, pool de conexiones,
                                  ect. */
                                new ReactorClientHttpConnector(
                                        HttpClient.create()
                                                /* Timeout máximo de respuesta de la API. Si excede 5 segundos, se lanza
                                                TimeoutException*/
                                                .responseTimeout(Duration.ofSeconds(5))
                                )
                        )
                        // Construye el WebClient con la configuración especificada
                        .build();
    }
}
