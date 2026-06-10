# Microservicio de Ventas y Pagos - Clínica Veterinaria VetNova

## 1. Descripción del Proyecto
Este microservicio gestiona las transacciones comerciales de VetNova. Se encarga de procesar las ventas de insumos y medicamentos, administrar los pagos, emitir boletas y gestionar las devoluciones. Se integra estrechamente con el Microservicio de Inventario para garantizar la consistencia del stock físico de la clínica.

## 2. Arquitectura y Tecnologías
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.4.x
* **Persistencia:** MySQL y Spring Data JPA
* **Comunicación:** Spring Cloud OpenFeign
* **Documentación:** Swagger / SpringDoc OpenAPI

## 3. Requisitos Previos y Ejecución
1. Iniciar XAMPP/Laragon con MySQL en el puerto 3306.
2. Levantar la aplicación. La base de datos `db_ventas` se creará sola.
* **Puerto local:** `8088`
* **Swagger:** `http://localhost:8088/doc/swagger-ui/index.html`

## 4. Integración Rest (Feign)
* **Consume a MS Inventario:**
  1. Valida disponibilidad de stock antes de generar la venta (`/validar-stock`).
  2. Ordena descontar el stock físico al procesar el pago (`/descontar-stock`).

---

## 5. Guía de Pruebas (Postman)

### A. Registrar una Venta (Estado PENDIENTE)
* **Método:** `POST`
* **URL:** `http://localhost:8088/api/v1/ventas/registrar`
* **Body (JSON):**
```json
{
    "idCliente": 2,
    "idProducto": 15,
    "cantidad": 2,
    "montoTotal": 24990.0
}
B. Procesar el Pago de la Venta (Pasa a PAGADA)
Método: PUT
URL: http://localhost:8088/api/v1/ventas/1/pagar
Body: none
C. Emitir Boleta
Método: GET
URL: http://localhost:8088/api/v1/ventas/1/boleta (Nota: Si intentas emitir boleta de una venta PENDIENTE, el sistema arrojará un error 500 controlado).
D. Registrar Devolución (Pasa a DEVUELTA)
Método: PUT
URL: http://localhost:8088/api/v1/ventas/1/devolucion
Body: none
