# Microservicio de Agenda
Administra la reserva de horas médicas de la clínica VetNova. Permite la programación, confirmación, cancelación y reprogramación de citas.

- **Puerto de ejecución:** `8084`
- **Base de Datos:** `db_agenda` (MySQL)

## Endpoints Principales
- `POST /api/v1/citas` : Reserva una nueva hora médica.
- `PUT /api/v1/citas/{id}/reprogramar` : Modifica la fecha de una cita existente.
- `PUT /api/v1/citas/{id}/cancelar` : Anula una hora reservada.
- `PUT /api/v1/citas/{id}/confirmar` : Confirma la llegada del paciente.

## Matriz de Eventos (Arquitectura Asíncrona)
Este servicio no bloquea la creación de citas buscando datos en otros MS vía REST, todo se comunica por dominio.
- **Eventos que PUBLICA:** `CitaAgendada`, `CitaCancelada`, `CitaConfirmada`, `InasistenciaRegistrada`
- **Eventos que CONSUME:** `HorarioConfigurado`, `BoxActualizado`, `EstadoMascotaActualizado`, `ClienteInactivado`

## Cómo ejecutar el proyecto
1. Enciende MySQL y crea la base de datos `db_agenda`.
2. Ejecuta `mvn clean install` para limpiar el espacio de trabajo.
3. Inicia el microservicio ejecutando `AgendaApplication.java` en tu IDE.

## Cómo probar en Postman
1. Realiza una petición `POST` a `http://localhost:8084/api/v1/citas`.
2. Envía en el **Body** (formato JSON): `{"idCliente": 10, "idMascota": 22, "fechaHora": "2026-06-15T10:30:00"}`.
3. Obtendrás una respuesta ResponseDTO confirmando el estado inicial "AGENDADA" y por detrás se emitirá asíncronamente el evento `CitaAgendada` envuelto en su cápsula estándar.