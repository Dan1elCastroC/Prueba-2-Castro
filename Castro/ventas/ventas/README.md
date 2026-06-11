# Microservicio de Ventas y Pagos
Administra el procesamiento financiero de la clínica, gestionando el registro de ventas, confirmación de pagos, emisión de boletas y control de devoluciones.

- **Puerto de ejecución:** `8088`
- **Base de Datos:** `db_ventas` (MySQL)

## Endpoints Principales
- `POST /api/v1/ventas/registrar` : Ingresa una nueva intención de compra.
- `PUT /api/v1/ventas/{id}/pagar` : Procesa el pago y confirma la venta.
- `PUT /api/v1/ventas/{id}/devolucion` : Registra anulación/devolución.
- `GET /api/v1/ventas/{id}/boleta` : Emite el documento tributario.

## Matriz de Eventos (Arquitectura Asíncrona)
Las validaciones de stock físico se delegan al Inventario de forma eventual.
- **Eventos que PUBLICA:** `VentaConfirmada`, `PagoConfirmado`, `DevolucionRegistrada`
- **Eventos que CONSUME:** `StockActualizado`, `ClienteInactivado`

## Cómo ejecutar el proyecto
1. Levanta tu servidor MySQL y verifica que exista el esquema `db_ventas`.
2. Compila el proyecto ejecutando: `mvn clean install`.
3. Inicia la aplicación desde tu entorno de desarrollo ejecutando `VentasApplication.java`.

## Cómo probar en Postman
1. Prepara un `POST` a `http://localhost:8088/api/v1/ventas/registrar`.
2. En el **Body** (raw -> JSON) envía el RequestDTO: `{"idCliente": 5, "idProducto": 12, "cantidad": 2, "montoTotal": 25000.0}`.
3. Recibirás un HTTP 201 Created. Luego puedes usar el ID retornado para hacer un `PUT` a `/api/v1/ventas/{id}/pagar` y ver cómo el sistema dispara el evento `VentaConfirmada`.
