package com.proyectoUno.controller;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.service.External.interfaces.UsuarioServiceExternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioServiceExternal usuarioServiceExternal;

    @Autowired
    public UsuarioController(UsuarioServiceExternal usuarioServiceExternal){

        this.usuarioServiceExternal=usuarioServiceExternal;
    }


    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerUsuarios(){

        List<UsuarioResponseDTO> usuarios = usuarioServiceExternal.encontrarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable UUID id){

        UsuarioResponseDTO usuario= usuarioServiceExternal.encontrarUsuarioPorId(id);

        return  ResponseEntity.ok(usuario);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuarioPorId(@PathVariable UUID id){

        usuarioServiceExternal.eliminarUsuarioPorId(id);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable UUID id, @Valid @RequestBody UsuarioActualizarDTO request){

        UsuarioResponseDTO usuarioActualizado = usuarioServiceExternal.actualizarUsuario(id,request);

        return  ResponseEntity.ok(usuarioActualizado);


    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearUsuario(@Valid @RequestBody List<UsuarioCrearRequestDTO> requests){

        usuarioServiceExternal.guardarUsuario(requests);
    }

}
