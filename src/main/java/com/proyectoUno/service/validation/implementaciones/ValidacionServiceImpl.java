package com.proyectoUno.service.validation.implementaciones;

import com.proyectoUno.exception.ListaDeEntidadesVaciaException;
import com.proyectoUno.exception.PaginaDeEntidadesVaciaException;
import com.proyectoUno.service.validation.interfaces.ValidacionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidacionServiceImpl implements ValidacionService {
    @Override
    public <T> void validarListaDeLibrosNoVacia(List<T> lista, String nombreEntidad) {

        if (lista == null || lista.isEmpty() ){
            throw new ListaDeEntidadesVaciaException("No se encontraron"+ nombreEntidad);
        }



    }

    @Override
    public <T> void validarPaginaNoVacia(Page<T> pagina, String nombreEntidad) {

        if (pagina == null || pagina.isEmpty()){

            throw new PaginaDeEntidadesVaciaException("No se encontraron"+ nombreEntidad);
        }

    }
}
