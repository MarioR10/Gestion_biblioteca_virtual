package com.proyectoUno.security.service;

/*
   Servicio que carga los datos del usuario desde una fuente (BD, API, etc.).
   DEBE implementar UserDetailsService.
   una fuente externa (por ejemplo, tu base de datos). Solo tiene un metodo importante */

import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository){
        this.usuarioRepository=usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        return
                // 1. Busca el usuario por email en la base de datos.
                //    El metodo findByEmail() devuelve un Optional<Usuario>.
                //    - Si encuentra un usuario, el Optional contendrá el objeto Usuario.
                //    - Si no encuentra nada, devolverá un Optional.empty() (una "caja vacía").
                usuarioRepository.findByEmail(username)

                // 2. Transforma el Optional<Usuario> a Optional<CustomUserDetails> usando .map().
                //    **El metodo .map() primero verifica si el Optional contiene un valor.**
                //    Si hay un Usuario presente, el lambda lo convierte a CustomUserDetails y .map() lo envuelve en un nuevo Optional.
                //    Si el Optional está vacío, .map() devuelve un Optional<CustomUserDetails> vacío sin ejecutar el lambda.

                // 3. Extrae CustomUserDetails o lanza una excepción.
                //    Si el Optional no está vacío, devuelve el CustomUserDetails.
                //    De lo contrario, lanza UsernameNotFoundException.

                        .map(usuario -> new CustomUserDetails(usuario))
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado con email: "+username));
    }
}

