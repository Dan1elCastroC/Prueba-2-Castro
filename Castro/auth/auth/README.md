# Microservicio de Autenticación (Auth-Service)
Componente central del ecosistema VetNova encargado de la gestión de identidades, emisión de tokens de seguridad (JWT) y validación de credenciales bajo una arquitectura Stateless.

- **Puerto de ejecución:** `8081`
- **Base de Datos:** `Auth_Service` (MySQL)

## Endpoints Principales
- `POST /api/v1/auth/login` : Valida credenciales y retorna el Token JWT.
- `POST /api/v1/auth/recuperar-password` : Inicia el flujo de recuperación de cuenta.

## Matriz de Eventos (Arquitectura Asíncrona)
Este microservicio opera de manera desacoplada mediante un Event Broker.
- **Eventos que PUBLICA:** `LoginExitoso`, `LoginFallido`, `PasswordRecuperado`
- **Eventos que CONSUME:** `UsuarioCreado`, `UsuarioDesactivado`, `RolAsignado`

## Cómo ejecutar el proyecto
1. Asegúrate de tener MySQL ejecutándose localmente (ej. vía XAMPP/Laragon) y crear la base de datos `Auth_Service`.
2. En la terminal, ubícate en la raíz del proyecto y ejecuta para limpiar e instalar: `mvn clean install`
3. Levanta la aplicación ejecutando la clase principal `AuthApplication.java` desde tu IDE, o usando el comando: `mvn spring-boot:run`

## Cómo probar en Postman
1. Abre Postman y crea una petición `POST` hacia `http://localhost:8081/api/v1/auth/login`.
2. En la pestaña **Body**, selecciona **raw** y formato **JSON**.
3. Envía el payload con las credenciales: `{"email": "admin@vetnova.cl", "password": "password123"}`.
4. El sistema validará la información y retornará un HTTP 200 OK con tu Token JWT.