# FERREMAS Backend API

Backend desarrollado para la Evaluación 2 del ramo **Integración de Plataformas (ASY5131)**.

El proyecto corresponde a una **API REST** para la empresa **FERREMAS**, orientada a la gestión de productos, usuarios, pedidos, estados de pedidos, pagos simulados e integración pública de productos para sistemas externos.

---

## Integrantes

- Fernando Ronda
- Benjamín Lackington
- Vicente Soto

---

## Contexto del proyecto

FERREMAS es una distribuidora de productos de ferretería y construcción que requiere modernizar su operación mediante una plataforma de comercio electrónico.

En esta segunda etapa del proyecto se construye el backend de la solución, aplicando una arquitectura por capas y exponiendo una API REST que permite integrar funcionalidades de catálogo, pedidos, usuarios y pagos.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.5.13
- Maven
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Postman
- Visual Studio Code

---

## Arquitectura del proyecto

El backend utiliza una arquitectura por capas, separando responsabilidades de la siguiente forma:

```text
controller   -> expone los endpoints REST
service      -> contiene la lógica de negocio
repository   -> accede a la base de datos
model        -> define las entidades del sistema
dto          -> define objetos de entrada y salida
integration  -> simula integración con servicios externos
config       -> carga datos iniciales
```

Estructura principal:

```text
src/main/java/com/ferremas/backend
│
├── config
│   └── DataLoader.java
│
├── controller
│   ├── ProductoController.java
│   ├── ProductoApiController.java
│   ├── PedidoController.java
│   └── UsuarioController.java
│
├── dto
│   ├── LoginResponse.java
│   ├── PedidoRequest.java
│   └── PagoRequest.java
│
├── integration
│   └── WebpayService.java
│
├── model
│   ├── Producto.java
│   ├── Pedido.java
│   └── Usuario.java
│
├── repository
│   ├── ProductoRepository.java
│   ├── PedidoRepository.java
│   └── UsuarioRepository.java
│
└── service
    ├── ProductoService.java
    ├── PedidoService.java
    └── UsuarioService.java
```

---

## Cómo ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

### 2. Entrar a la carpeta del backend

```bash
cd backend
```

### 3. Ejecutar el proyecto

```bash
mvn spring-boot:run
```

El backend se levantará en:

```text
http://localhost:8080
```

---

## Base de datos

El proyecto utiliza **H2 Database en memoria**, lo que permite probar el backend sin instalar una base de datos externa.

Cada vez que se inicia la aplicación, el sistema carga datos iniciales mediante `DataLoader`, incluyendo productos y usuarios demo.

---

## Usuarios demo

| Rol | Email | Contraseña |
|---|---|---|
| Cliente | cliente@ferremas.cl | 1234 |
| Vendedor | vendedor@ferremas.cl | 1234 |
| Bodeguero | bodeguero@ferremas.cl | 1234 |
| Contador | contador@ferremas.cl | 1234 |
| Administrador | admin@ferremas.cl | 1234 |

---

## Módulo Productos

Permite administrar productos de FERREMAS.

### Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/productos` | Listar todos los productos |
| GET | `/productos/{id}` | Buscar producto por ID |
| POST | `/productos` | Crear producto |
| PUT | `/productos/{id}` | Actualizar producto |
| DELETE | `/productos/{id}` | Eliminar producto |

### Ejemplo JSON para crear producto

```json
{
  "codigoProducto": "FER-99999",
  "marca": "Bosch",
  "codigoMarca": "BOS-99999",
  "nombre": "Atornillador Bosch",
  "modelo": "GSR 120-LI",
  "categoria": "Herramientas Eléctricas",
  "descripcion": "Atornillador inalámbrico profesional",
  "precio": 69990,
  "stock": 20
}
```

---

## API pública de productos

Esta API está pensada para integración con sucursales internas o sistemas externos.

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/productos` | Listar productos disponibles |
| GET | `/api/productos/{codigoProducto}` | Buscar producto por código FERREMAS |
| GET | `/api/productos/marca/{marca}` | Buscar productos por marca |

### Ejemplos

```http
GET http://localhost:8080/api/productos
GET http://localhost:8080/api/productos/FER-12345
GET http://localhost:8080/api/productos/marca/Bosch
```

---

## Módulo Usuarios

Permite registrar usuarios y realizar login básico.

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/usuarios` | Listar usuarios |
| POST | `/usuarios/registro` | Registrar usuario |
| POST | `/usuarios/login` | Iniciar sesión |

### Registro de usuario

```json
{
  "nombre": "Cliente Prueba",
  "email": "clienteprueba@ferremas.cl",
  "password": "1234",
  "rol": "CLIENTE"
}
```

### Login

```http
POST http://localhost:8080/usuarios/login?email=cliente@ferremas.cl&password=1234
```

Respuesta esperada:

```json
{
  "id": 1,
  "nombre": "Cliente Demo",
  "email": "cliente@ferremas.cl",
  "rol": "CLIENTE"
}
```

El login devuelve un DTO de respuesta, evitando exponer la contraseña del usuario.

---

## Módulo Pedidos

Permite crear pedidos, validar stock, descontar productos y gestionar estados operativos.

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/pedidos` | Listar pedidos |
| GET | `/pedidos/{id}` | Buscar pedido por ID |
| POST | `/pedidos` | Crear pedido |
| PUT | `/pedidos/pagar` | Procesar pago |
| PUT | `/pedidos/{id}/aprobar` | Aprobar pedido |
| PUT | `/pedidos/{id}/rechazar` | Rechazar pedido |
| PUT | `/pedidos/{id}/preparar` | Preparar pedido |
| PUT | `/pedidos/{id}/listo-despacho` | Marcar pedido listo para despacho |
| PUT | `/pedidos/{id}/despachar` | Despachar pedido |

### Crear pedido con despacho

```json
{
  "productoId": 1,
  "cantidad": 2,
  "tipoEntrega": "DESPACHO",
  "direccion": "Santateresa407"
}
```

### Crear pedido con retiro

```json
{
  "productoId": 2,
  "cantidad": 1,
  "tipoEntrega": "RETIRO"
}
```

---

## Pago simulado / integración Webpay

El proyecto incluye una capa de integración llamada `WebpayService`, la cual simula el procesamiento de pagos con tarjeta, débito o crédito.

Esta implementación permite mantener separada la lógica de pedidos de la lógica de pago, dejando preparado el backend para una futura integración real con Webpay.

### Procesar pago

```http
PUT http://localhost:8080/pedidos/pagar
```

Body:

```json
{
  "pedidoId": 1,
  "metodoPago": "TARJETA"
}
```

Métodos aceptados:

```text
TARJETA
DEBITO
CREDITO
TRANSFERENCIA
```

Los métodos `TARJETA`, `DEBITO` y `CREDITO` se procesan mediante `WebpayService` simulado.

El método `TRANSFERENCIA` se considera como pago validado internamente.

---

## Flujo de estados del pedido

El pedido sigue el siguiente flujo operativo:

```text
PENDIENTE -> PAGADO -> APROBADO -> EN_PREPARACION -> LISTO_DESPACHO -> DESPACHADO
```

También puede quedar como:

```text
RECHAZADO
```

---

## Pruebas con Postman

Se creó una colección en Postman llamada:

```text
FERREMAS Backend API
```

La colección contiene pruebas para:

- Usuarios
- Productos
- Pedidos
- Pagos
- Estados del pedido
- API pública de productos

### Flujo recomendado para demostración

```text
1. GET /api/productos
2. POST /usuarios/login
3. POST /pedidos
4. PUT /pedidos/pagar
5. PUT /pedidos/{id}/aprobar
6. PUT /pedidos/{id}/preparar
7. PUT /pedidos/{id}/listo-despacho
8. PUT /pedidos/{id}/despachar
9. GET /pedidos/{id}
```

---

## Funcionalidades implementadas

- Gestión de productos.
- Consulta pública de productos para integración externa.
- Registro de usuarios.
- Login básico con respuesta DTO.
- Roles de usuario.
- Creación de pedidos.
- Validación de stock.
- Descuento automático de stock.
- Selección de tipo de entrega.
- Validación de dirección para despacho.
- Pago simulado.
- Estados operativos del pedido.
- Carga automática de datos iniciales.
- Arquitectura por capas.

---

## Repositorios del proyecto

Según las instrucciones de la evaluación, el sistema se divide en dos repositorios:

```text
ferremas-backend  -> API REST desarrollada en Spring Boot
ferremas-frontend -> interfaz web del comercio electrónico
```

Este repositorio corresponde al backend de la solución.

---

## Observaciones

La integración con Webpay se encuentra actualmente simulada mediante `WebpayService`.

La arquitectura queda preparada para reemplazar esta simulación por la API real de Webpay en una etapa posterior del desarrollo.