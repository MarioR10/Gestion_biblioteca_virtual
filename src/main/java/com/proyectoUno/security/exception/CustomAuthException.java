package com.proyectoUno.security.exception;

public class CustomAuthException extends RuntimeException {
  public CustomAuthException(String message) {
    super(message);
  }
}
