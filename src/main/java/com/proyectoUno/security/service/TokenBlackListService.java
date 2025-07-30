package com.proyectoUno.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlackListService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TokenBlackListService( RedisTemplate<String, String> redisTemplate){

        this.redisTemplate=redisTemplate;
    }

    /**
     * Metodo que agrega tokens a la lista negra de Redis
     * @param token El token (Strin), de access o refresh que se quiere invalidar
     * @param duration Tiempo que sera invalidado hasta su expiracion natural
     */
    public void blackListToken(String token, Duration duration){

        /*
        En Redis los datos se almacenan como un par clave- valor, y se hace la busqueda mediante la clave. La clave
        es un identificador unico por el cual se busca. De manera que definimos como clave el token (string) porque es el
        identificador que nosotros queremos saber si esta presente en la lista, de esat manera la verificacion es rapida,
        como valor podemos poner algo simbolico, pues lo que nos importa es el token.
         */

        //Aqui asiganamos clave: <el String token>, valor < blacklisted> y el tiempo de duracion del token en la lista negra
        //antes de ser eliminado por Redis, gracias a TTL
        redisTemplate.opsForValue().set(token, "blacklisted", duration);

    }

    public boolean isTokenBlackListed(String token) {

        /*
         * Consultamos la lista negra para confirmar si el token está presente o no.
         *
         * La variable "existe" es de tipo Boolean, una clase envolvente (wrapper) de Java.
         * El metodo redisTemplate.hasKey(token) puede devolver:
         *   - Boolean.TRUE  → si la clave (token) existe en Redis
         *   - Boolean.FALSE → si no existe
         *   - null          → si ocurre un error o Redis no responde
         *
         * Las constantes Boolean.TRUE y Boolean.FALSE son objetos estáticos predefinidos
         * que representan los valores primitivos true y false, respectivamente.
         * Por eso almacenamos el resultado en una variable de tipo Boolean (objeto),
         * y no en un tipo primitivo boolean (que no acepta null).
         */
        Boolean existe = redisTemplate.hasKey(token);

        /*
         * El valor obtenido es un objeto Boolean.
         * Como el metodo isTokenBlackListed devuelve un booleano primitivo,
         * usamos Boolean.TRUE.equals(existe) para convertirlo de forma segura.
         *
         * Esto devuelve true solo si "existe" es Boolean.TRUE.
         * Si "existe" es Boolean.FALSE o null, devuelve false.
         * De esta forma evitamos posibles NullPointerException.
         */
        return Boolean.TRUE.equals(existe);
    }

}
