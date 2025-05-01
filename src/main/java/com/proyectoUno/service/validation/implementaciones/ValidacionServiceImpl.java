package com.proyectoUno.service.validation.implementaciones;

import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.exception.ListaDeEntidadesVaciaException;
import com.proyectoUno.exception.PaginaDeEntidadesVaciaException;
import com.proyectoUno.service.validation.interfaces.ValidacionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidacionServiceImpl implements ValidacionService {
    @Override
    public <T> void validarListaDeLibrosNoVacia(List<T> lista, String nombreEntidad) {

        if (lista == null || lista.isEmpty() ){
            throw new ListaDeEntidadesVaciaException("No se encontraron "+ nombreEntidad + " disponibles");
        }

    }

    @Override
    public <T> void validarPaginaNoVacia(Page<T> pagina, String nombreEntidad) {

        if (pagina == null || pagina.isEmpty()){

            throw new PaginaDeEntidadesVaciaException("No se encontraron "+ nombreEntidad + " disponibles");
        }

    }

    @Override
    public <T> T validarExistencia(Optional<T> entidad, String nombreEntidad) {

        T entidadOptional = entidad.orElseThrow(

                () -> new EntidadNoEncontradaException("no ha sido encontrado"));

        return  entidadOptional;
    }


}
