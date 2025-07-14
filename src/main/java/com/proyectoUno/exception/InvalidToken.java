package com.proyectoUno.exception;

import com.zaxxer.hikari.util.ClockSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidToken extends RuntimeException {

  //Loger que permite capturar errores/ excepciones en consola
  private final Logger logger = LoggerFactory.getLogger(InvalidToken.class);

  //ID de la excepcion
  private final String internalCode = "INVALID_TOKEN";

  public InvalidToken(String message){

    super(message);
  }
    }

