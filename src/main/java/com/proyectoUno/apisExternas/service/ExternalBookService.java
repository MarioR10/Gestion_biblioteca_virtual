package com.proyectoUno.apisExternas.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoUno.exception.ConflictException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import  com.proyectoUno.apisExternas.dto.ExternalBookDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de consumir la API externa de Google Books.
 * Utiliza WebClient, el cliente HTTP moderno y reactivo de Spring,
 * recomendado para nuevas aplicaciones y como sucesor de RestTemplate.
 * * NOTA: WebClient es asíncrono por diseño. El uso de .block() aquí
 * fuerza al hilo de ejecución actual a esperar de forma síncrona el
 * resultado. Esto se hace para integrar la librería reactiva en un flujo
 * síncrono tradicional de Spring MVC (no reactivo).
 */

@Service
public class ExternalBookService {

    private final WebClient webClient; //WebClient para peticiones HTTP.
    private final Logger logger= LoggerFactory.getLogger(ExternalBookService.class);
    //ObjectMapper de Jackson para trabajar con JSON, realiza serealizacion  (objeto a JSON) y deserealizacion (JSON a objeto)
    private final ObjectMapper objectMapper;

    @Value("${google.books.api.key}")
    private String apiKey;

    @Value("${google.books.api.url}")
    private String apiUrl;


    public ExternalBookService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient=webClient;
        this.objectMapper=objectMapper;
    }
    /*/*/
    //Metodo principal que consulta la API por ISBN y devuelve un ExternalBookDTO
    //Maneja errores HTTP, JSON y casos de no encontrado
    public ExternalBookDTO obtenerDetallesDelLibro(String isbn){
        logger.info("Consultando Google Books API para el ISBN {}", isbn);

        /*/*/
        //Intenetamos obtener los datos de la API (en formato JSON) y luego lo convertimos a nuestra entidad LibroCrearDTO
        //para mapearla a la base de datos
        try{
            // Configuramos una solicitud HTTP GET para obtener los datos de la API.
            // Almacenamos la respuesta en un String, que contendrá el cuerpo de la respuesta HTTP. (JSON)
            String responseBody= webClient.get()

                    // Construye dinámicamente la URL de la petición
                    .uri(   //Lambda que permite convertir un UriBuilder a una URI
                            uriBuilder -> uriBuilder
                            // Agrega el parámetro 'q' con el ISBN (ejemplo: q=isbn:978-0134685991)
                            .queryParam("q","isbn:"+isbn)
                            // Agrega el parámetro 'key' con la llave de la API (ejemplo: key=la-api-key)
                            .queryParam("key",apiKey)
                            // Construye la URL final con la base y los parámetros
                            .build())

                    //Envia la solicitud y recibe la respuesta
                    .retrieve()

                    //Se encarga del manejo de errores HTTP, si hay un error presente en la solicitud.
                    .onStatus(status -> status.isError(),response ->{
                        logger.error("Error al consultar la API, codigo: {}",response.statusCode());

                        //Es el mecanismo que se utiliza para presentar errores en un flujo asincrono.
                        //No se utiliza throw que interrumpe el flujo de ejecucion.
                        return Mono.error(new ConflictException("Hubo un conlicto al intentar consultar la API externa"));
                    })

                    //Convierte el body de la HTTP en un Mono del tipo String
                    .bodyToMono(String.class)

                    //vuelve sincrono el proceso, el hilo actual queda bloqueado hasta obtener la respuesta
                    .block();

      //------------------------------ ESTRUCTURA DE ARBOL (NODOS) utilizando JsonNode de jackson------------------//

            /*
            JsonNode nos permite representar un JSON completo como un conjunto de nodos,
            donde cada objeto, array o valor es un nodo.
            `objectMapper.readTree(json)` convierte el JSON completo en un nodo raíz (`root`),
            desde el cual podemos navegar y acceder a los campos o listas ( otros nodos) que necesitemos
            sin mapearlo directamente a clases Java.
            */
            JsonNode root = objectMapper.readTree(responseBody); //JSON completo de la API externa como nodo raiz



            /*
             * Accedemos al nodo "totalItems" del nodo raíz (root), que representa la cantidad
             * de resultados devueltos por la API para el ISBN consultado.
             * Si el valor es 0, significa que no hay resultados, por lo que lanzamos
             * una EntidadNoEncontradaException.
             */

            if(root.path("totalItems").asInt()==0){

                logger.error("No se encontraron libros con ISBN: {}", isbn);
                throw  new EntidadNoEncontradaException(
                        "Libro con ISBN: "+isbn+", no encontrado");
            }

            /*
             * Accedemos al nodo "items" del nodo raíz, que es un arreglo de resultados devueltos por la API.
             * Seleccionamos el primer elemento del arreglo ([0]), que representa un JSON completo con varios campos (nodos),
             * no solo los datos del libro. Luego accedemos al nodo "volumeInfo" de dicho JSON, que contiene específicamente
             * todos los detalles del libro: título, autores, fecha de publicación, categorías, etc (nodos).
             * Esto nos permite extraer únicamente los valores que necesitamos, sin tener que mapear todo el JSON completo.
             */
            JsonNode item= root.path("items").get(0).path("volumeInfo");

            /*
             * Consultamos el nodo "authors", que está dentro de "item" (el nodo "volumeInfo").
             * Verificamos si "authors" es un array (representado como un ArrayNode).
             * Cada elemento de ese array también es un nodo, por lo que lo convertimos a texto (con asText())
             * y lo agregamos a un ArrayList, que es la estructura más común para trabajar con listas en Java.
             */
            List<String> authors = new ArrayList<>();
            if (item.path("authors").isArray()){
                item.path("authors").forEach( a -> authors.add(a.asText()));
            }

            /*
             * Consultamos el nodo "categories", que está dentro de "item" (el nodo "volumeInfo").
             * Verificamos si "categories" es un array (representado como un ArrayNode).
             * Cada elemento de ese array también es un nodo, por lo que lo convertimos a texto (con asText())
             * y lo agregamos a un ArrayList, que es la estructura más común para trabajar con listas en Java.
             */
            List<String> categories = new ArrayList<>();
            if (item.path("categories").isArray()){
                item.path("categories").forEach( c -> categories.add(c.asText()));
            }

            //Creamos y retornamos el DTO
            //Usamos asText(null) para manejar campos opcionales (null si no existen).
            return new ExternalBookDTO(

                    item.path("title").asText(null),
                    authors,
                    item.path("description").asText(null),
                    isbn,
                    categories,
                    item.path("publishedDate").asText(null)
            );

        // EXPLICACIÓN: Capturamos excepciones (ej. JSON malformado, red caída).
        }catch ( ConflictException e){
            logger.error("Error al procesar la respuesta de la API para ISBN: {}", isbn, e);
            throw e;

        }catch (EntidadNoEncontradaException e){

                logger.error("No se pudo encontrar el libro con ISBN: {}", isbn, e);
            throw e;

        } catch (Exception e) {
            throw new ConflictException("Error inesperado al consultar la API");
        }

    }
}
