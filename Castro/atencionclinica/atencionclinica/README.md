# Microservicio de Atención Clínica
Núcleo médico de VetNova. Encargado de administrar fichas clínicas, registrar diagnósticos, tratamientos, emitir recetas y certificados de los pacientes.

- **Puerto de ejecución:** `8083`
- **Base de Datos:** `db_atencion_clinica` (MySQL)

## Endpoints Principales
- `POST /api/v1/fichas` : Crea una nueva ficha clínica (Retorna DTO).
- `GET /api/v1/fichas/{id}` : Consulta una ficha por ID.
- `POST /api/v1/atenciones` : Registra una nueva atención/diagnóstico.
- `PUT /api/v1/atenciones/{id}/receta` : Emite una receta médica asociada.

## Matriz de Eventos (Arquitectura Asíncrona)
Completamente desacoplado, sin uso de dependencias síncronas REST.
- **Eventos que PUBLICA:** `AtencionRegistrada`, `RecetaEmitida`, `CertificadoEmitido`, `VacunaRegistrada`
- **Eventos que CONSUME:** `MascotaRegistrada`, `EstadoMascotaActualizado`

## Cómo ejecutar el proyecto
1. Verifica que MySQL esté activo en el puerto 3306 y la BD creada.
2. Limpia el proyecto con: `mvn clean install`
3. Ejecuta la clase `AtencionclinicaApplication.java` desde tu IDE.

## Cómo probar en Postman
1. Crea una petición `POST` hacia `http://localhost:8083/api/v1/fichas`.
2. En la pestaña **Body** (raw -> JSON), envía: `{"idMascota": 1, "observaciones": "Control general sano"}`.
3. El sistema validará con Jakarta Bean Validation y devolverá un HTTP 201 Created con el FichaClinicaResponseDTO.