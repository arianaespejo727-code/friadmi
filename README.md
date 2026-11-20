# FRIADMI - Sistema Administrativo

Sistema administrativo web para la gestión integral de una empresa. Permite controlar usuarios, clientes, productos, ventas y reportes desde un panel central.

## ¿Qué hace el sistema?

- **Login seguro** con roles de usuario
- **Dashboard** con resumen de ventas, usuarios y actividad
- **Gestión de usuarios** - crear, editar y eliminar usuarios
- **Gestión de clientes y productos**
- **Control de ventas y reportes**
- **Subida de archivos** e imágenes

## Tecnologías usadas

- Java 17 con Spring Boot
- Thymeleaf + HTML / CSS / Bootstrap
- PostgreSQL como base de datos
- Maven

## Cómo correr el proyecto en local

1. Clonar el repositorio:
   git clone https://github.com/arianaespejo727-code/friadmi.git

2. Crear la base de datos en PostgreSQL y ejecutar el archivo `database.sql`

3. Configurar en `src/main/resources/application.properties` tu usuario y contraseña de PostgreSQL

4. Correr el proyecto:
   mvn spring-boot:run

5. Entrar en el navegador a:
   http://localhost:8080

## Despliegue

El proyecto está desplegado en Render con PostgreSQL.

## Autora

Ariana Espejo - Proyecto Friadmi
