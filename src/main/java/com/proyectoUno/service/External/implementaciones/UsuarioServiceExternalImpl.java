package com.proyectoUno.service.External.implementaciones;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.maper.usuario.UsuarioRequestMapperStruct;
import com.proyectoUno.maper.usuario.UsuarioResponseMapperStruct;
import com.proyectoUno.service.External.interfaces.UsuarioServiceExternal;
import com.proyectoUno.service.Internal.interfaces.UsuarioServiceInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceExternalImpl implements UsuarioServiceExternal {


    private final UsuarioResponseMapperStruct usuarioResponseMapper;
    private final UsuarioServiceInternal usuarioServiceInternal;
    private final UsuarioRequestMapperStruct usuarioRequestMapper;


    //Inyeccion de dependencias
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public UsuarioServiceExternalImpl(UsuarioResponseMapperStruct usuarioResponseMapper, UsuarioServiceInternal usuarioServiceInternal, UsuarioRequestMapperStruct usuarioRequestMapper){
        this.usuarioResponseMapper = usuarioResponseMapper;
        this.usuarioServiceInternal = usuarioServiceInternal;
        this.usuarioRequestMapper = usuarioRequestMapper;
    }


    @Override
    public UsuarioResponseDTO  encontrarUsuarioPorId(UUID id) {

        //Obtenemos ususario
        Usuario usuario = usuarioServiceInternal.encontrarUsuarioPorId(id);

        //Convertimos a DTO
        return usuarioResponseMapper.toResponseDTO(usuario);
    }

    @Override
    public void eliminarUsuarioPorId(UUID id) {

        //Eliminamos usuario
        usuarioServiceInternal.eliminarUsuarioPorId(id);
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(UUID id,UsuarioActualizarDTO usuarioActualizar) {

        //Obtenemos la entidad parcial (al crearla solo le asignamos los campos del DTO), esta no se agurda en la BD
        Usuario datosActualizar= usuarioRequestMapper.toEntity(usuarioActualizar);

        //Pasamos la entidad parcial a nuestro servicio interno para que actualice los datos
        Usuario usuarioActualizado= usuarioServiceInternal.actualizarUsuario(id, datosActualizar);

        //guarda el usuario actualizado
        return usuarioResponseMapper.toResponseDTO(usuarioActualizado); // JPA actualizará automáticamente por @Transactional


    }

    @Override
    public void crearUsuario(List<UsuarioCrearRequestDTO> usuarioDTO) {

        //convertir lista DTO a entidad
        List<Usuario> usuarios= usuarioRequestMapper.toEntityList(usuarioDTO);

        //Guardar Usuario
        usuarioServiceInternal.crearUsuario(usuarios);
    }

    @Override
    public Page<UsuarioResponseDTO> encontrarUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioServiceInternal.encontrarUsuarios(pageable);
        return usuarioResponseMapper.toResponsePageDTO(usuarios);
    }


}
