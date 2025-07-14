package com.proyectoUno.exception;

public class InvalidToken extends RuntimeException {
  public InvalidToken(String message) {
    super(message);
  }
}
