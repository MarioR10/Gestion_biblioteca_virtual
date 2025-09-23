package com.proyectoUno.controller;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la entidad Usuario.
 * Proporciona endpoints para CRUD y búsquedas paginadas.
 * La seguridad de acceso a cada método se define en SecurityConfiguration.
 */
@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Constructor con inyección de dependencias del servicio de Usuario.
     * @param usuarioService servicio para operaciones sobre usuarios
     */
    @Autowired
    public UsuarioController(UsuarioService usuarioService){

        this.usuarioService = usuarioService;
    }

    // ==========================
    // Métodos CRUD básicos
    // ==========================


    /**
     * Obtiene un usuario por su ID.
     * @param id UUID del usuario
     * @return DTO con los datos del usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable UUID id){
        UsuarioResponseDTO usuario= usuarioService.encontrarUsuarioPorId(id);
        return  ResponseEntity.ok(usuario);
    }

    /**
     * Elimina un usuario por su ID.
     * @param id UUID del usuario a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuarioPorId(@PathVariable UUID id){
        usuarioService.eliminarUsuarioPorId(id);
    }

    /**
     * Actualiza los datos de un usuario existente.
     * @param id      UUID del usuario a actualizar
     * @param request DTO con los campos a actualizar
     * @return DTO con los datos actualizados del usuario
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable UUID id, @Valid @RequestBody UsuarioActualizarDTO request){
        UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuario(id,request);
        return  ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * Crea uno o varios usuarios.
     * @param requests lista de DTOs con los datos de los usuarios a crear
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void crearUsuario(@Valid @RequestBody List<UsuarioCrearRequestDTO> requests){
        usuarioService.crearUsuario(requests);
    }

    /**
     * Obtiene todos los usuarios con paginación.
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de usuarios
     */
    @GetMapping()
    public ResponseEntity<Page< UsuarioResponseDTO>> encontrarUsuarios(
            @PageableDefault(page = 0, size = 8) Pageable pageable
            ){

         Page<UsuarioResponseDTO> usuarios = usuarioService.encontrarUsuarios(pageable);
         return  ResponseEntity.ok(usuarios);
    }
}
