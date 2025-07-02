# API de Reportes - Perfunlandia

Microservicio que centraliza los datos desde las APIs de `ventas` y `detalleventa` para generar reportes integrados del sistema.

---

## 🛠️ Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.x
- Maven
- MySQL
- Swagger / OpenAPI 3
- Spring WebClient (para consumir otras APIs)
- DTOs estructurados (Output)
- HATEOAS

---

## ⚙️ Configuración

- **Puerto local:** `8083`
- **Swagger UI:** [`http://localhost:8083/swagger-ui.html`](http://localhost:8083/swagger-ui.html)

---

## 📌 Endpoints principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/reportes` | Listar todos los reportes |
| `GET` | `/reportes/{id}` | Obtener reporte por ID |
| `POST` | `/reportes` | Crear un reporte con datos cruzados de ventas y detalle |
| `DELETE` | `/reportes/{id}` | Eliminar un reporte |

---

## 🔗 Dependencias externas

- **GET a API Ventas:** `/ventas`
- **GET a API DetalleVenta:** `/detalleventas`

---

## 🧪 Pruebas

Swagger UI te permite probar todo visualmente. También puedes usar Postman.
