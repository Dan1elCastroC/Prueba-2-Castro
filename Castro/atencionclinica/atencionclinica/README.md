# Microservicio de Atención Clínica - Clínica Veterinaria VetNova

## 1. Descripción del Proyecto
Este microservicio es el núcleo médico del ecosistema distribuido VetNova. Se encarga de gestionar las fichas clínicas de los pacientes, así como de registrar diagnósticos, tratamientos, emitir recetas y emitir certificados. Fue diseñado respetando el patrón CSR (Controller-Service-Repository) y evitando recursividades infinitas en los JSON de salida mediante anotaciones bidireccionales (`@JsonManagedReference`).

## 2. Arquitectura y Tecnologías
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.4.x
* **Persistencia:** MySQL y Spring Data JPA
* **Comunicación:** Spring Cloud OpenFeign
* **Documentación:** Swagger / SpringDoc OpenAPI

## 3. Requisitos Previos y Ejecución
1. Iniciar XAMPP/Laragon con MySQL en el puerto 3306.
2. La base de datos `db_atencion_clinica` se creará automáticamente gracias al parámetro `createDatabaseIfNotExist=true` en el `application.properties`.
3. Levantar la aplicación.
* **Puerto local:** `8083`
* **Swagger:** `http://localhost:8083/doc/swagger-ui/index.html`

## 4. Guía de Pruebas (Links y JSON para Postman)

### A. Crear una nueva Ficha Clínica (HU-030)
* **Método:** `POST`
* **URL:** `http://localhost:8083/api/v1/fichas`
* **Body (raw - JSON):**

```json
{
    "idMascota": 5,
    "idVeterinario": 2,
    "motivoConsulta": "Control de rutina y vacunación"
}
B. Registrar Diagnóstico / Crear Atención (HU-033)
Método: POST
URL: http://localhost:8083/api/v1/atenciones
Body (raw - JSON):
{
    "descripcion": "Paciente presenta cuadro leve de otitis. Se observa inflamación.",
    "idVeterinario": 2,
    "fichaClinica": {
        "idFicha": 1
    }
}
C. Registrar Tratamiento en una Atención (HU-034)
Método: PUT
URL: http://localhost:8083/api/v1/atenciones/1/tratamiento
Body (raw - JSON):
{
    "tratamiento": "Aplicar gotas óticas antibióticas cada 12 horas por 7 días."
}
D. Emitir Receta Médica (HU-038)
Método: PUT
URL: http://localhost:8083/api/v1/atenciones/1/receta
Body (raw - JSON):
{
    "recetaMedica": "1. Otomax (Gotas) - 1 frasco. 2. Antiinflamatorio oral (5mg)."
}
E. Emitir Certificado Médico (HU-039)
Método: PUT
URL: http://localhost:8083/api/v1/atenciones/1/certificado
Body (raw - JSON):
{
    "detalleCertificado": "Se certifica que el paciente ha sido evaluado y se encuentra en óptimas condiciones de salud general para viajar."
}
F. Consultar Ficha y Atenciones Completas
Método: GET
URL: http://localhost:8083/api/v1/fichas/id/1 (Nota: Esto devolverá la ficha clínica junto con un arreglo de todos los diagnósticos, tratamientos y recetas asociadas).
