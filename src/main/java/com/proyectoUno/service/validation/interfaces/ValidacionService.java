package com.proyectoUno.service.validation.interfaces;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ValidacionService {

    <T> void validarListaDeLibrosNoVacia(List<T> lista, String nombreEntidad);
    <T> void validarPaginaNoVacia(Page<T> pagina, String nombreEntidad);
    <T> T  validarExistencia(Optional<T> entidad, String nombreEntidad);
}
