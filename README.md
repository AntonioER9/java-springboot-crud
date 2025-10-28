# Employee CRUD API with CSV Bulk Upload

Una API RESTful para gestionar empleados con funcionalidad de carga masiva desde archivos CSV.

## Estructura del Proyecto

```
src/main/java/com/springboot/cruddemo/
├── controller/          # Controladores REST
├── service/            # Lógica de negocio
├── repository/         # Acceso a datos
├── entity/            # Entidades JPA
├── dto/               # Data Transfer Objects
├── exception/         # Excepciones personalizadas
├── util/             # Utilidades y helpers
└── config/           # Configuración
```

## Endpoints Disponibles

### Operaciones CRUD Individuales

#### GET /api/employees
Obtener todos los empleados
```bash
curl -X GET http://localhost:8080/api/employees
```

#### GET /api/employees/{id}
Obtener empleado por ID
```bash
curl -X GET http://localhost:8080/api/employees/1
```

#### POST /api/employees
Crear nuevo empleado
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Carlos",
    "lastName": "Garcia",
    "email": "carlos.garcia@example.com"
  }'
```

#### PUT /api/employees
Actualizar empleado existente
```bash
curl -X PUT http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "firstName": "Carlos",
    "lastName": "Garcia",
    "email": "carlos.garcia@company.com"
  }'
```

#### PATCH /api/employees/{id}
Actualización parcial de empleado
```bash
curl -X PATCH http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nuevo.email@example.com"
  }'
```

#### DELETE /api/employees/{id}
Eliminar empleado
```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

### Carga Masiva CSV

#### POST /api/bulk/employees/upload
Cargar empleados desde archivo CSV

**Formato del CSV:**
```csv
firstName,lastName,email
Carlos,Garcia,carlos.garcia@example.com
María,Rodríguez,maria.rodriguez@example.com
Juan,Pérez,juan.perez@example.com
```

**Comando cURL:**
```bash
curl -X POST http://localhost:8080/api/bulk/employees/upload \
  -F "file=@employees.csv"
```

**Ejemplo de respuesta:**
```json
{
  "totalRecords": 3,
  "successfulRecords": 2,
  "failedRecords": 1,
  "errors": [
    "Line 3: Email already exists - juan.perez@example.com"
  ],
  "createdEmployees": [
    {
      "id": 1,
      "firstName": "Carlos",
      "lastName": "Garcia",
      "email": "carlos.garcia@example.com"
    },
    {
      "id": 2,
      "firstName": "María",
      "lastName": "Rodríguez",
      "email": "maria.rodriguez@example.com"
    }
  ]
}
```

## Validaciones CSV

- **Formato de archivo**: Solo se aceptan archivos .csv
- **Tamaño máximo**: 5MB
- **Cabeceras requeridas**: `firstName,lastName,email` (en ese orden exacto)
- **Validaciones de datos**:
  - Todos los campos son obligatorios
  - Email debe tener formato válido (contener @ y .)
  - No se permiten emails duplicados

## Códigos de Respuesta HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **204 No Content**: Eliminación exitosa
- **207 Multi-Status**: Carga masiva con éxito parcial
- **400 Bad Request**: Error en la solicitud o validación
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error interno del servidor

## Manejo de Errores

Todas las respuestas de error siguen el formato estándar:

```json
{
  "timestamp": "2025-10-28T18:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "path": "/api/employees/999"
}
```

## Características Implementadas

- ✅ CRUD completo con DTOs
- ✅ Manejo de excepciones centralizado
- ✅ Validaciones de datos
- ✅ Carga masiva desde CSV
- ✅ Detección de duplicados por email
- ✅ Respuestas HTTP consistentes
- ✅ Arquitectura en capas (Controller, Service, Repository)
- ✅ Mapeo automático Entity ↔ DTO

## Tecnologías Utilizadas

- Spring Boot 3
- Spring Data JPA
- Spring Web
- H2 Database (desarrollo)
- Maven

## Ejecución

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: http://localhost:8080

## Ejemplos de Archivos CSV para Pruebas

### employees.csv
```csv
firstName,lastName,email
Carlos,Garcia,carlos.garcia@example.com
María,Rodríguez,maria.rodriguez@example.com
Juan,Pérez,juan.perez@example.com
Ana,López,ana.lopez@example.com
Pedro,Martínez,pedro.martinez@example.com
```
