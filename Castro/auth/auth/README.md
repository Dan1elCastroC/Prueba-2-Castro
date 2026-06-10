# Microservicio de Autenticación (Auth) - Clínica Veterinaria VetNova

## 1. Descripción del Proyecto
Este microservicio centraliza la seguridad del ecosistema distribuido de la Clínica Veterinaria VetNova. Es un componente **100% Stateless** (sin estado y sin base de datos propia) encargado de validar credenciales, interceptar accesos no autorizados y emitir tokens JWT (JSON Web Tokens) para proteger las rutas del sistema. 

## 2. Integrantes del Equipo
* Daniel Castro
* Pamela Acuña
* Gabriel Martínez

## 3. Arquitectura y Tecnologías
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.4.x
* **Seguridad:** Spring Security y JWT (jjwt API)
* **Arquitectura:** Stateless (Sin persistencia local)
* **Comunicación:** Spring Cloud OpenFeign
* **Documentación API:** Swagger / SpringDoc OpenAPI
* **Herramientas:** Lombok, Jakarta Validation

## 4. Requisitos Previos
Para ejecutar este microservicio en un entorno local, necesitas:
1. Java JDK 21 instalado en tu sistema.
2. IDE de desarrollo (IntelliJ IDEA, Eclipse o VS Code).
*(Nota de Arquitectura: Tras la auditoría técnica, este microservicio fue desacoplado de la persistencia de datos. NO requiere iniciar MySQL ni configurar bases de datos, ya que delega la verificación de existencia de usuarios al MS Usuarios).*

## 5. Ejecución y Despliegue Local
El microservicio se levantará de forma ligera e independiente:
* **Puerto:** `8081`
* **Ruta Base API:** `http://localhost:8081/api/v1/auth`
* **Documentación Visual (Swagger):** `http://localhost:8081/doc/swagger-ui/index.html`

## 6. Integración y Comunicación (OpenFeign)
* **Consume a MS Usuarios:** Consulta vía Feign Client si el correo electrónico ingresado existe realmente en el sistema central antes de procesar la contraseña y emitir el token. *(Llamada actualmente comentada/simulada para pruebas locales independientes en la Fase 1).*

---

## 7. Guía de Pruebas (Links y JSON para Postman)

A continuación, se detallan las rutas exactas y los cuerpos (Body) necesarios para validar la seguridad del sistema y obtener tu token.

### A. Iniciar Sesión (Login) y Obtener Token JWT
* **Método:** `POST`
* **URL:** `http://localhost:8081/api/v1/auth/login`
* **Descripción:** Valida las credenciales del usuario y, si son correctas, devuelve un Token JWT firmado criptográficamente.
* **Body (raw - JSON):**
```json
{
    "email": "admin@vetnova.cl",
    "password": "password123"
}
(Nota de validación: Si ingresas una contraseña distinta a "password123" o dejas el correo vacío, el sistema interceptará el error automáticamente devolviendo un estado HTTP 401 Unauthorized o 400 Bad Request).
B. Recuperar Contraseña
Método: POST
URL: http://localhost:8081/api/v1/auth/recuperar-password
Descripción: Inicia el flujo de recuperación de contraseña para un correo electrónico específico.
Body (raw - JSON):
{
    "email": "admin@vetnova.cl"
}
