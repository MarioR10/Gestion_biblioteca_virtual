package com.proyectoUno.service.implementaciones;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadDuplicadaException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.exception.SolicitudConDuplicadosException;
import com.proyectoUno.maper.usuario.UsuarioRequestMapperStruct;
import com.proyectoUno.maper.usuario.UsuarioResponseMapperStruct;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar todas las operaciones relacionadas con usuarios.
 * Incluye:
 * 1. Creación de usuarios (individual o en lote) con validación de duplicados.
 * 2. Actualización de datos de usuario existentes.
 * 3. Eliminación de usuarios.
 * 4. Búsqueda de usuarios por ID o de forma paginada.
 * 5. Operaciones internas para acceder a entidades Usuario directamente.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final UsuarioResponseMapperStruct usuarioResponseMapper;
    private final UsuarioRequestMapperStruct usuarioRequestMapper;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * @param usuarioRepository       Repositorio JPA para acceder a los datos de usuarios.
     * @param usuarioResponseMapper   Mapper para convertir entidades Usuario a DTOs de respuesta.
     * @param usuarioRequestMapper    Mapper para convertir DTOs de solicitud a entidades Usuario.
     */
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioResponseMapperStruct usuarioResponseMapper, UsuarioRequestMapperStruct usuarioRequestMapper){
        this.usuarioRepository=usuarioRepository;
        this.usuarioResponseMapper = usuarioResponseMapper;
        this.usuarioRequestMapper = usuarioRequestMapper;
    }


    /**
     * Busca un usuario por su ID.
     * @param id Identificador del usuario.
     * @return DTO con la información del usuario.
     * @throws EntidadNoEncontradaException Si el usuario no existe.
     */
    @Override
    public UsuarioResponseDTO encontrarUsuarioPorId(UUID id) {

        //Encontramos el Usuario en la DB
        Usuario usuario= usuarioRepository.findById(id)
                .orElseThrow(()-> new EntidadNoEncontradaException("Usuario","ID",id));

        //Convertimos a DTO
        return  usuarioResponseMapper.toResponseDTO(usuario);

    }

    /**
     * Elimina un usuario por su ID.
     * @param id Identificador del usuario a eliminar.
     */
    @Override
    @Transactional
    public void eliminarUsuarioPorId(UUID id){

        //Encontramos el usuario
        Usuario usuario= this.encontrarUsuarioPorIdInterno(id);
        //Eliminamos el usuario encontrado
        usuarioRepository.delete(usuario);
    }

    /**
     * Actualiza los datos de un usuario existente.
     * @param id               ID del usuario a actualizar.
     * @param usuarioActualizar DTO con los campos a actualizar.
     * @return DTO con los datos actualizados del usuario.
     */
    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(UUID id, UsuarioActualizarDTO usuarioActualizar){

        //1. Buscamos el usuario existente en la base de datos
        Usuario usuario = encontrarUsuarioPorIdInterno(id);

        //2. Usar MapStruct para actualizar los campos no null del DTO, en la entidad ya existente
        usuarioRequestMapper.toUpdateEntity(usuarioActualizar,usuario);

        //3. Persistir la entidad actualizada
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        //4. Convertir a DTO
        return  usuarioResponseMapper.toResponseDTO(usuarioGuardado);
    }

    /**
     * Crea uno o más usuarios a partir de una lista de DTOs.
     * Realiza validaciones internas de duplicados y contra la base de datos.
     * @param usuariosDTO Lista de DTOs con los datos de los usuarios a crear.
     * @throws SolicitudConDuplicadosException Si hay emails duplicados en la lista.
     * @throws EntidadDuplicadaException       Si alguno de los emails ya existe en la base de datos.
     */
    @Transactional
    public void crearUsuario(List<UsuarioCrearRequestDTO> usuariosDTO){

        //convertir lista DTO a entidad
        List<Usuario> usuariosNuevos = usuarioRequestMapper.toEntityList(usuariosDTO);

        /*De todos los datos de los usuarios que vienen en la lista solo interesa el email para verificar duplicados,
       por lo que transformamos la lista usuarios  de tipo Usuario en una lista String que contenga solo los emails*/
        List<String> emailsNuevos =
                //stream() crea un flujo de datos de los elementos de una lista para que puedan ser procesados uno a uno
                usuariosNuevos.stream()
                        //map() transforma cada elemento del stream; aquí, convierte cada objeto Usuario en su email usando el metodo getEmail().
                        .map(Usuario::getEmail)
                        //collect() recolecta los emails (Strings) en una lista; es necesario porque los streams son temporales y los resultados se pierden si no se almacenan.
                        .collect(Collectors.toList());

        // FASE 1: VALIDACIÓN INTERNA DE LA SOLICITUD (verificamos que en la lista no vengan emails duplicados

        //1. Necesitamos un set (no permite elementos duplciados).
        Set<String> emailsUnicos = new HashSet<>();

        //2. Filtramos la lista , para encontrar los duplicados
        List<String> emailsDuplicados =

                //stream() crea un flujo de datos de los elementos de una lista para que puedan ser procesados uno a uno
                emailsNuevos.stream()

                        /*cada email se intenta añadir a la lista, el metodo !emailsUnicos.add(email)), regresa un booleano
                         TRUE -> si se agrego el elemento ; FALSE -> No se agrego el elemento. En la sentencia estamos negando ese resultado, de manera que cuando
                         haya duplicado (FALSE) pase a ser TRUE.
                         Se hace esto, debido a que el metodo filter() solo toma los elementos que devuelven TRUE, (en este caso serian solo los duplicados)
                         */
                        .filter(email -> !emailsUnicos.add(email))
                        .collect(Collectors.toList());

        if( !emailsDuplicados.isEmpty()){
            throw new SolicitudConDuplicadosException("email", emailsDuplicados);
        }

        // FASE 2: VALIDACIÓN CONTRA LA BASE DE DATOS (verificamos que la lista ya verificada no tenga duplicados con datos ya existentes en la DB)

        // 1. buscamos en la base de datos usuarios con los correos nuevos
        List<Usuario> usuariosExistentes = usuarioRepository.findByEmailIn(emailsNuevos);

        //2. verificamos si la lista no esta vacia (significa que si existen duplicados)
        if(!usuariosExistentes.isEmpty()){

            //Obtenemos los emails que ya existen
            List<String> emailsExistentes =
                    //stream() crea un flujo de datos de los elementos de una lista para que puedan ser procesados uno a uno
                    usuariosExistentes.stream()

                            //map() transforma cada elemento del stream; aquí, convierte cada objeto Usuario en su email usando el metodo getEmail().
                            .map(Usuario::getEmail)
                            //collect() recolecta los emails (Strings) en una lista; es necesario porque los streams son temporales y los resultados se pierden si no se almacenan.
                            .collect(Collectors.toList());
            //lanzamos excepcion
            throw new EntidadDuplicadaException("Usuario","email",emailsExistentes);
        }

        // FASE 3: PERSISTENCIA

        // Guardamos los nuevos usuarios verificados
        usuarioRepository.saveAll(usuariosNuevos);

    }

    // ==========================================
    // MÉTODOS CON PAGINACIÓN
    // ==========================================

    /**
     * Obtiene todos los usuarios de forma paginada.
     * @param pageable Información de paginación.
     * @return Página de DTOs con los usuarios.
     */
    @Override
    public Page<UsuarioResponseDTO> encontrarUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);

        return usuarioResponseMapper.toResponsePageDTO(usuarios);
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    /**
     * Método interno que busca un usuario por ID y devuelve la entidad completa.
     * Útil para operaciones internas donde se requiere la entidad directamente.
     * @param id Identificador del usuario.
     * @return Entidad Usuario.
     * @throws EntidadNoEncontradaException Si el usuario no existe.
     */
    @Override
    public Usuario encontrarUsuarioPorIdInterno(UUID id){
        //Encontramos el Usuario en la DB
        Usuario usuario= usuarioRepository.findById(id)
                .orElseThrow(()-> new EntidadNoEncontradaException("Usuario","ID",id));

        return usuario;
    }
}
