# FERREMAS Backend

Backend desarrollado para el proyecto **FERREMAS**, correspondiente a la evaluación de Integración de Plataformas.

El sistema implementa una API REST para gestionar productos, usuarios, pedidos, pagos, suscripciones, conversión de divisas e integración con servicios externos.

---

## Descripción del proyecto

FERREMAS es una distribuidora de productos de ferretería y construcción que requiere una solución de comercio electrónico para digitalizar su proceso de ventas, permitir compras en línea y mejorar la gestión de pedidos entre clientes, vendedores, bodegueros y contadores.

Este backend permite:

- Consultar productos disponibles.
- Registrar usuarios y controlar acceso por roles.
- Crear pedidos asociados a clientes.
- Gestionar estados del pedido.
- Procesar pagos mediante transferencia o integración con Mercado Pago.
- Consultar el valor del dólar mediante integración con Banco Central de Chile.
- Registrar suscripciones de clientes.
- Proteger contraseñas mediante BCrypt.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.5.13
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- Lombok
- BCrypt
- Mercado Pago SDK
- API Banco Central de Chile
- Postman
- Git y GitHub

---

## Arquitectura utilizada

El backend utiliza una arquitectura por capas:

```text
Controller → Service → Repository → Model
```

### Capas principales

- **Controller:** expone endpoints REST.
- **Service:** contiene reglas de negocio y validaciones.
- **Repository:** acceso a datos mediante Spring Data JPA.
- **Model:** entidades del sistema.
- **DTO:** objetos para entrada y salida de datos.
- **Config:** configuración inicial, carga de datos demo y seguridad.

---

## Funcionalidades principales

### Productos

Permite gestionar y consultar productos de FERREMAS.

Cada producto contiene:

- Código de producto
- Marca
- Código de marca
- Nombre
- Modelo
- Categoría
- Descripción
- Precio
- Stock

---

### Usuarios y roles

El sistema cuenta con usuarios demo para probar el flujo completo.

| Rol | Email | Contraseña |
|---|---|---|
| Cliente | cliente@ferremas.cl | 1234 |
| Vendedor | vendedor@ferremas.cl | 1234 |
| Bodeguero | bodeguero@ferremas.cl | 1234 |
| Contador | contador@ferremas.cl | 1234 |
| Administrador | admin@ferremas.cl | 1234 |

Roles implementados:

- CLIENTE
- VENDEDOR
- BODEGUERO
- CONTADOR
- ADMINISTRADOR

---

### Seguridad de contraseñas

Las contraseñas se almacenan cifradas utilizando **BCrypt**.

El login compara la contraseña ingresada por el usuario con la contraseña cifrada almacenada en base de datos mediante `passwordEncoder.matches()`.

Esto evita guardar contraseñas en texto plano.

---

### Pedidos

El sistema permite:

- Crear pedidos asociados a un usuario cliente.
- Validar stock disponible.
- Seleccionar retiro en tienda o despacho a domicilio.
- Asociar producto, cantidad, total, método de pago y cliente.
- Controlar estados del pedido durante todo el flujo operacional.

Estados utilizados:

```text
PENDIENTE
PAGADO
APROBADO
EN_PREPARACION
LISTO_DESPACHO
DESPACHADO
RECHAZADO
```

---

### Flujo operacional del pedido

```text
Cliente crea pedido
        ↓
Cliente paga
        ↓
Vendedor aprueba o rechaza
        ↓
Bodeguero prepara pedido
        ↓
Bodeguero marca pedido listo
        ↓
Contador registra entrega final
```

---

### Pagos

El backend permite:

- Pago por transferencia bancaria.
- Integración con Mercado Pago Checkout Pro.
- Creación de preferencia de pago.
- Simulación de pago con tarjetas de prueba.
- Registro del método de pago en el pedido.

---

### Conversión de divisas

El backend consume la API del Banco Central de Chile para consultar el valor del dólar observado.

Esto permite que el frontend muestre el precio del producto en pesos chilenos y también una referencia aproximada en dólares.

---

### Suscripciones

Permite registrar correos de clientes para recibir ofertas, noticias y descuentos.

El endpoint valida:

- Correo vacío.
- Formato básico del correo.
- Correos duplicados.

---

## Endpoints principales

### Productos

```http
GET /api/productos
GET /api/productos/{codigoProducto}
GET /api/productos/marca/{marca}

GET /productos
POST /productos
PUT /productos/{id}
DELETE /productos/{id}
```

---

### Usuarios

```http
GET /usuarios
POST /usuarios/registro
POST /usuarios/login?email={email}&password={password}
```

---

### Pedidos

```http
GET /pedidos
GET /pedidos/{id}
POST /pedidos
PUT /pedidos/pagar
PUT /pedidos/{id}/aprobar
PUT /pedidos/{id}/rechazar
PUT /pedidos/{id}/preparar
PUT /pedidos/{id}/listo-despacho
PUT /pedidos/{id}/despachar
```

---

### Divisas

```http
GET /api/divisas/dolar
```

---

### Mercado Pago

```http
POST /api/pagos/mercadopago/preferencia
```

---

### Suscripciones

```http
POST /api/suscripciones
```

---

## Ejemplo de creación de pedido

```json
{
  "productoId": 1,
  "usuarioId": 1,
  "cantidad": 1,
  "tipoEntrega": "RETIRO",
  "direccion": ""
}
```

---

## Ejemplo de pago por transferencia

```json
{
  "pedidoId": 1,
  "metodoPago": "TRANSFERENCIA"
}
```

---

## Ejemplo de preferencia Mercado Pago

```json
{
  "pedidoId": 1,
  "titulo": "Taladro Percutor Bosch",
  "cantidad": 1,
  "precioUnitario": 89090
}
```

---

## Variables de entorno

Este proyecto utiliza variables de entorno para proteger credenciales externas.

```properties
BCENTRAL_USER=usuario_banco_central
BCENTRAL_PASS=password_banco_central
MP_ACCESS_TOKEN=access_token_mercado_pago
```

En `application.properties` se utilizan así:

```properties
server.port=${PORT:8080}

bcentral.user=${BCENTRAL_USER:}
bcentral.pass=${BCENTRAL_PASS:}
bcentral.serie.dolar=F073.TCO.PRE.Z.D

mercadopago.access.token=${MP_ACCESS_TOKEN:}

server.error.include-message=always
```

> Importante: no se deben subir credenciales reales a GitHub.

---

## Cómo ejecutar el backend

Desde PowerShell:

```powershell
cd C:\Users\Moonlab\OneDrive\Escritorio\backend

$env:BCENTRAL_USER="TU_USUARIO_BANCO_CENTRAL"
$env:BCENTRAL_PASS="TU_PASSWORD_BANCO_CENTRAL"
$env:MP_ACCESS_TOKEN="TU_ACCESS_TOKEN_MERCADO_PAGO"

.\mvnw.cmd spring-boot:run
```

El backend queda disponible en:

```text
http://localhost:8080
```

---

## Base de datos

El proyecto utiliza **H2 en memoria** para facilitar la ejecución y presentación.

Cada vez que se reinicia el backend, se cargan datos iniciales mediante `DataLoader`.

Incluye:

- 4 productos demo.
- 5 usuarios demo.
- Contraseñas cifradas con BCrypt.

---

## Pruebas realizadas

Se realizaron pruebas funcionales con Postman y desde el frontend para validar:

- Consulta de productos.
- Login por roles.
- Registro de suscripciones.
- Creación de pedidos.
- Validación de stock.
- Pago por transferencia.
- Creación de preferencia de pago con Mercado Pago.
- Flujo de aprobación por vendedor.
- Preparación por bodeguero.
- Registro de entrega por contador.
- Consulta de dólar desde Banco Central.

---

## Evidencias recomendadas para presentación

- Captura de productos desde `GET /api/productos`.
- Captura de login funcionando.
- Captura de creación de pedido.
- Captura de pago por transferencia.
- Captura de preferencia generada en Mercado Pago.
- Captura de valor dólar desde Banco Central.
- Captura de panel vendedor, bodeguero y contador.
- Captura de compilación exitosa con Maven.

---

## Mejoras futuras

- Implementar JWT para autenticación segura.
- Agregar Spring Security completo con autorización por roles.
- Persistir datos en MySQL o PostgreSQL.
- Implementar Webhooks de Mercado Pago para confirmar pagos automáticamente.
- Agregar Swagger/OpenAPI para documentación interactiva.
- Agregar pruebas unitarias con JUnit y Mockito.
- Agregar pruebas de integración para endpoints REST.
- Agregar pipeline CI/CD con GitHub Actions.
- Mejorar manejo global de errores.
- Implementar pedidos con múltiples productos.
- Agregar historial de pedidos por cliente.
- Agregar reportes financieros para administrador y contador.
- Desplegar backend en Render, Railway u otro servicio cloud.

---

## Autores

Proyecto desarrollado por:

**Vicente Soto**  
**Fernando Ronda**  
**Benjamin Lackington**  

Ingeniería en Informática  
Duoc UC
