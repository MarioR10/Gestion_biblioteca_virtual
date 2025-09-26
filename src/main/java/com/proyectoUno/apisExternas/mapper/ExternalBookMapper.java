package com.proyectoUno.apisExternas.mapper;

import com.proyectoUno.apisExternas.dto.ExternalBookDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapeador de MapStruct para convertir el DTO de la API externa (ExternalBookDTO)
 * al DTO de creación de nuestra entidad interna (LibroCrearRequestDTO).
 * * Utiliza métodos 'default' y la anotación @Named para aplicar lógica de transformación
 * compleja (como el manejo de listas a campos únicos y el parseo de fechas).
 */
@Mapper(componentModel = "spring")
public interface ExternalBookMapper {

    // Se define el mapeo de campos. Se usa 'source' y 'target' cuando los nombres difieren.
    // 'qualifiedByName' invoca los métodos 'default' de la interfaz para transformaciones específicas.
    @Mapping(source = "title", target = "titulo", defaultValue = "Unknow title")
    @Mapping(source = "authors", target = "autor", qualifiedByName = "getFirstAuthor")
    @Mapping(source = "categories", target = "categoria", qualifiedByName = "getFirstCategory")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "publishedDate", target = "anioDePublicacion",qualifiedByName = "parsePublishedDate")
    LibroCrearRequestDTO toLibroCrearRequestDTO(ExternalBookDTO externalBook);


    // --- Métodos de Transformación de Datos (@Named) ---

    /**
     * Transforma una lista de autores (que viene de la API externa) a un único String de autor,
     * dado que la base de datos solo almacena uno.
     * Retorna "Unknown Author" si la lista de autores es nula o vacía.
     */
    @Named("getFirstAuthor")
    default String getFirstAuthor(List<String> authors){

        return authors !=null && !authors.isEmpty()? authors.get(0): "Unknow Author";
    }

    /**
     * Transforma una lista de categorías (que viene de la API externa) a un único String de categoría.
     * * dado que la base de datos solo almacena una.
     * Retorna "Unknown Category" si la lista está vacía o es nula.
     */
    @Named("getFirstCategory")
    default String getFirstCategories(List<String> categories){

        return categories !=null && !categories.isEmpty()? categories.get(0): "Unknow Author";
    }


    /**
     * Parsea el campo 'publishedDate' (ej. "2023-10-25") para extraer únicamente el año como Integer.
     * Retorna 0 (un valor de respaldo) si la fecha es nula, vacía o si ocurre un fallo en el parseo.
     * Esto garantiza que el campo 'anioDePublicacion' (que presumiblemente es NOT NULL) siempre tenga un valor.
     */
    @Named("parsePublishedDate")
    default Integer parsePublishedDate(String publishedDate) {
        if (publishedDate == null || publishedDate.isEmpty()) return 0;
        try {
            return Integer.parseInt(publishedDate.split("-")[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
