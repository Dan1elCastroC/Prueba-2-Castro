# Microservicio de Agenda - Clínica Veterinaria VetNova

## 1. Descripción del Proyecto
Este microservicio es el encargado de gestionar la reserva, reprogramación, cancelación y confirmación de citas médicas en el ecosistema distribuido de la Clínica Veterinaria VetNova. Está desarrollado bajo la arquitectura CSR (Controller - Service - Repository) y se comunica de manera síncrona con otros dominios del sistema para asegurar la integridad de los datos agendados.

## 2. Integrantes del Equipo
* Daniel Castro
* Pamela Acuña
* Gabriel Martínez

## 3. Arquitectura y Tecnologías
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.4.x
* **Persistencia:** MySQL y Spring Data JPA (Hibernate)
* **Testing:** JUnit, Mockito y H2 Database (Memoria)
* **Comunicación:** Spring Cloud OpenFeign
* **Documentación API:** Swagger / SpringDoc OpenAPI
* **Herramientas:** Lombok, Jakarta Validation

## 4. Requisitos Previos y Configuración de Base de Datos
Para ejecutar este microservicio en un entorno local, necesitas:
1. Java JDK 21 instalado en tu sistema.
2. XAMPP o Laragon con el módulo de MySQL iniciado (Puerto 3306).
3. IDE de desarrollo (IntelliJ IDEA, Eclipse o VS Code).

**Creación de la Base de Datos:**
Antes de ejecutar el proyecto, debes crear la base de datos estandarizada en MySQL. Abre tu gestor (phpMyAdmin, HeidiSQL, etc.) y ejecuta:
```sql
CREATE DATABASE db_agenda;
```
*Nota: El microservicio está configurado con `ddl-auto=update`, por lo que las tablas se crearán automáticamente al iniciar la aplicación por primera vez.*

## 5. Ejecución y Despliegue Local
El microservicio se levantará de forma independiente para no colisionar con el resto del ecosistema:
* **Puerto:** `8086`
* **Ruta Base API:** `http://localhost:8086/api/v1/citas`
* **Documentación Visual (Swagger):** `http://localhost:8086/doc/swagger-ui/index.html`

## 6. Integración y Comunicación (OpenFeign)
Para mantener la integridad referencial sin compartir bases de datos, este microservicio actúa como **Consumidor** y **Proveedor**:
* **Consume a MS Clientes:** Valida que el dueño exista antes de agendar (Evita clientes fantasma).
* **Consume a MS Mascotas:** Valida que el paciente exista antes de agendar.
* **Provee a MS Notificaciones:** Expone las citas de las próximas 24 horas para el envío de recordatorios automáticos (Endpoint `/manana`).

---

## 7. Guía de Pruebas (Links y JSON para Postman)

A continuación, se detallan las rutas exactas y los cuerpos (Body) necesarios para validar cada endpoint funcional (RF09, RF10).

*(Asegúrate de incluir tu Token JWT en la pestaña **Authorization -> Bearer Token** en Postman si la seguridad ya está habilitada).*

### A. Agendar una Nueva Cita
* **Método:** `POST`
* **URL:** `http://localhost:8086/api/v1/citas/agendar`
* **Descripción:** Crea una nueva cita médica y le asigna automáticamente el estado "AGENDADA".
* **Body (raw - JSON):**
```json
{
    "idCliente": 1,
    "idMascota": 3,
    "idVeterinario": 5,
    "fechaHora": "2026-06-15T15:30:00",
    "motivo": "Control post-operatorio del paciente"
}
```

### B. Confirmar Asistencia del Paciente
* **Método:** `PUT`
* **URL:** `http://localhost:8086/api/v1/citas/1/confirmar` *(Reemplaza '1' por un ID existente)*
* **Descripción:** Cambia el estado interno de la cita a "CONFIRMADA".
* **Body:** *none* (No requiere cuerpo).

### C. Reprogramar la Cita
* **Método:** `PUT`
* **URL:** `http://localhost:8086/api/v1/citas/1/reprogramar`
* **Descripción:** Modifica la fecha y hora de la cita.
* **Body (raw - JSON):**
```json
{
    "nuevaFechaHora": "2026-06-20T10:00:00"
}
```

### D. Cancelar la Cita
* **Método:** `PUT`
* **URL:** `http://localhost:8086/api/v1/citas/1/cancelar`
* **Descripción:** Cambia el estado de la cita médica a "CANCELADA".
* **Body:** *none* (No requiere cuerpo).

### E. Consultar Todas las Citas
* **Método:** `GET`
* **URL:** `http://localhost:8086/api/v1/citas`
* **Descripción:** Devuelve una lista (JSON array) con todas las citas registradas en el sistema.

### F. Buscar una Cita Específica
* **Método:** `GET`
* **URL:** `http://localhost:8086/api/v1/citas/1`
* **Descripción:** Devuelve el detalle completo de una sola cita filtrada por su ID.

### G. Consultar Citas de Mañana (Uso Interno)
* **Método:** `GET`
* **URL:** `http://localhost:8086/api/v1/citas/manana`
* **Descripción:** Endpoint interno consumido por el Microservicio de Notificaciones para el envío automático de recordatorios (HU-061).
```