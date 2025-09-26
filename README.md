# Gestor de Biblioteca

Este proyecto es un **gestor de biblioteca** enfocado principalmente en el personal de la biblioteca, aunque los usuarios también pueden consultar los libros disponibles. Su objetivo principal es facilitar la **administración de libros, préstamos y cuentas de usuarios**, integrando automatización mediante **APIs externas** para mejorar la eficiencia y optimizar procesos internos.

Es un proyecto personal desarrollado para **practicar y demostrar diferentes habilidades en Java y Spring Boot**, aplicando buenas prácticas de desarrollo.

---

## Características principales

- **CRUD completo de libros**: Permite agregar, editar, eliminar y consultar libros.
- **Integración con APIs externas**: Automatiza la creación de libros utilizando información de terceros (por ejemplo, Google Books API) para reducir el tiempo de ingreso de datos.
- **Gestión de usuarios**: Registro, autenticación y administración de cuentas de usuarios.
- **Gestión de préstamos**: Registrar, actualizar y controlar los préstamos de libros de forma completa.
- **Autenticación JWT**: Seguridad mediante tokens JWT para proteger las rutas del backend.
- **Búsquedas y consultas eficientes**: Uso de **Redis** para optimizar consultas y mejorar el rendimiento de la aplicación.
- **Manejo centralizado de excepciones**: Controladores globales que gestionan errores de manera uniforme para un flujo más robusto y predecible.
- **Buenas prácticas de arquitectura**: Aplicación de **patrones de diseño**, **principios SOLID** y **Clean Architecture** para mantener un código mantenible y escalable.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Security (JWT)
- WebClient para consumir APIs externas
- Jackson (JSON parsing)
- Redis para caching y optimización de consultas
- Maven

---

## Objetivo del proyecto

El proyecto busca servir como un **sistema de gestión integral para bibliotecas**, centralizando la información de libros, usuarios y préstamos, mientras se implementan **buenas prácticas de desarrollo backend** y se integran herramientas modernas para mejorar eficiencia y rendimiento.
