![ci-cd](https://github.com/ROMANOS-SIHRA/Backend_ECIEXPRESS_KAPPA_2025-2/actions/workflows/ci-cd.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Azure](https://img.shields.io/badge/Azure-App%20Service-0078D4)
[![API Status](https://img.shields.io/website?url=https%3A%2F%2Fsirha-backend-api.azurewebsites.net%2Factuator%2Fhealth)](https://ECIEXPRESS-backend-api.azurewebsites.net/actuator/health)


![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-green?logo=mongodb)
![Maven](https://img.shields.io/badge/Maven-3.9+-red?logo=apache-maven)
![JUnit](https://img.shields.io/badge/JUnit-5-yellow?logo=junit5)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?logo=swagger)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-blue?logo=github-actions)


![Code Coverage](https://img.shields.io/badge/Coverage-85%25-brightgreen)
![Quality Gate](https://img.shields.io/badge/Quality%20Gate-Passed-success?logo=sonarqube)
![Security Rating](https://img.shields.io/badge/Security-A-brightgreen?logo=sonarqube)
![Maintainability](https://img.shields.io/badge/Maintainability-A-brightgreen?logo=sonarqube)
![Technical Debt](https://img.shields.io/badge/Tech%20Debt-Low-green)




![Deployment](https://img.shields.io/badge/Deployment-Automated-success)
![Environment](https://img.shields.io/badge/Environment-Production-red)
![Response Time](https://img.shields.io/badge/Response%20Time-<200ms-brightgreen)

![HTTPS](https://img.shields.io/badge/HTTPS-Enabled-green?logo=letsencrypt)
![CORS](https://img.shields.io/badge/CORS-Configured-blue)
![API](https://img.shields.io/badge/API-RESTful-orange)
![Authentication](https://img.shields.io/badge/Auth-JWT-purple)

![GitHub](https://img.shields.io/badge/GitHub-Repository-black?logo=github)
![VS Code](https://img.shields.io/badge/IDE-VS%20Code-blue?logo=visual-studio-code)
![Postman](https://img.shields.io/badge/Testing-Postman-orange?logo=postman)


# 📚 ECIEXPRESS

> <b>Gestionamiento de compras en papelerias y cafeterias</b>

---

## 📑 Tabla de Contenidos

1. 👤 [Integrantes](#1--integrantes)
2. 🎯 [Objetivo del Proyecto](#2--objetivo-del-proyecto)
3. ⚡ [Funcionalidades principales](#3--funcionalidades-principales)
4. 📋 [Manejo de Estrategia de versionamiento y branches](#4--manejo-de-estrategia-de-versionamiento-y-branches)
    - 4.1 [Convenciones para crear ramas](#41-convenciones-para-crear-ramas)
    - 4.2 [Convenciones para crear commits](#42-convenciones-para-crear-commits)
5. ⚙️ [Tecnologías utilizadas](#5--tecnologias-utilizadas)
6. 🧩 [Funcionalidad](#6--funcionalidad)
7. 📊 [Diagramas](#7--diagramas)
    - 7.1 🟩 [Diagrama de Contexto](#71--diagrama-de-contexto)
    - 7.2 🟦 [Diagrama de Casos de Uso](#72--diagrama-de-casos-de-uso)
    - 7.3 🟨 [Diagrama de Clases](#73--diagrama-de-clases)
    - 7.4 🟥 [Diagrama de Componentes — General](#74--diagrama-de-componentes--general)
    - 7.5 🟨 [Diagrama de Componentes — Específico (Backend)](#75--diagrama-de-componentes--especifico-backend)
    - 7.6 🟩 [Diagrama de Base de Datos (MongoDB)](#76--diagrama-de-base-de-datos-mongodb)
    - 7.7 🛰️ [Diagrama de Despliegue](#77--diagrama-de-despliegue)
8. 🌐 [Endpoints expuestos y su información de entrada y salida](#8--endpoints-expuestos-y-su-informacion-de-entrada-y-salida)
9. ⚠️ [Manejo de Errores](#9--manejo-de-errores)
10. 🧪 [Evidencia de las pruebas y cómo ejecutarlas](#10--evidencia-de-las-pruebas-y-como-ejecutarlas)
11. 🗂️ [Código de la implementación organizado en las respectivas carpetas](#11--codigo-de-la-implementacion-organizado-en-las-respectivas-carpetas)
12. 📝 [Código documentado](#12--codigo-documentado)
13. 🧾 [Pruebas coherentes con el porcentaje de cobertura expuesto](#13--pruebas-coherentes-con-el-porcentaje-de-cobertura-expuesto)
14. 🚀 [Ejecución del Proyecto](#14--ejecucion-del-proyecto)
15. ☁️ [Evidencia de CI/CD y Despliegue en Azure](#15--evidencia-de-cicd-y-despliegue-en-azure)
16. 🤝 [Contribuciones y agradecimientos](#16--contribuciones-y-agradecimientos)


---

## 1. 👤 Integrantes:

- Daniel Rodríguez
- Julián Arenas
- Belén Quintero
- Marlio Charry
- Juan Pablo Contreras

## 2. 🎯 Objetivo del Proyecto

En la actualidad, las cafeterias y papelerias dentro de nuestro campus universitario se presentan serias dificultades
operativas durante las horas pico. Estudiantes, docentes y personal administrativo deben enfrentar largas filas y
esperas prolongadas para adquirir sus alimentos o materiales pedidos, lo que genera gran perdida de tiempo, generando
retrasos a clases, desorganizacon y una mala experiencia tanto para los usuarios como para los trabajadores.

El modelo de atención presencial genera mucha agromelación, errores en pedidos y pagos, poca trazabilidad en las ventas,
generando poca eficiencia operativa. Por lo cual se requiere un sistema digital que optimice los procesos de compra, para
reducir los tiempos de espera y mejorando la experiencia de todos.

---

## 3. ⚡ Funcionalidades principales



## 4. 📋 Manejo de Estrategia de versionamiento y branches

### Estrategia de Ramas (Git Flow)

![img.png](docs/gitFlow.png)
### Ramas y propósito
- Manejaremos GitFlow, el modelo de ramificación para el control de versiones de Git

#### `main`
- **Propósito:** rama **estable** con la versión final (lista para demo/producción).
- **Reglas:**
    - Solo recibe merges desde `release/*` y `hotfix/*`.
    - Cada merge a `main` debe crear un **tag** SemVer (`vX.Y.Z`).
    - Rama **protegida**: PR obligatorio, 1–2 aprobaciones, checks de CI en verde.

#### `develop`
- **Propósito:** integración continua de trabajo; base de nuevas funcionalidades.
- **Reglas:**
    - Recibe merges desde `feature/*` y también desde `release/*` al finalizar un release.
    - Rama **protegida** similar a `main`.

#### `feature/*`
- **Propósito:** desarrollo de una funcionalidad, refactor o spike.
- **Base:** `develop`.
- **Cierre:** se fusiona a `develop` mediante **PR**


#### `release/*`
- **Propósito:** congelar cambios para estabilizar pruebas, textos y versiones previas al deploy.
- **Base:** `develop`.
- **Cierre:** merge a `main` (crear **tag** `vX.Y.Z`) **y** merge de vuelta a `develop`.
- **Ejemplo de nombre:**  
  `release/1.3.0`

#### `hotfix/*`
- **Propósito:** corregir un bug **crítico** detectado en `main`.
- **Base:** `main`.
- **Cierre:** merge a `main` (crear **tag** de **PATCH**) **y** merge a `develop` para mantener paridad.
- **Ejemplos de nombre:**  
  `hotfix/fix-blank-screen`, `hotfix/css-broken-header`


---

### 4.1 Convenciones para **crear ramas**

#### `feature/*`
**Formato:**
```
feature/[nombre-funcionalidad]-ECIEXPRESS_[codigo-jira]
```

**Ejemplos:**
- `feature/readme_ECIEXPRESS-34`

**Reglas de nomenclatura:**
- Usar **kebab-case** (palabras separadas por guiones)
- Máximo 50 caracteres en total
- Descripción clara y específica de la funcionalidad
- Código de Jira obligatorio para trazabilidad

#### `release/*`
**Formato:**
```
release/[version]
```
**Ejemplo:** `release/1.3.0`

#### `hotfix/*`
**Formato:**
```
hotfix/[descripcion-breve-del-fix]
```
**Ejemplos:**
- `hotfix/corregir-pantalla-blanca`
- `hotfix/arreglar-header-responsive`

---

### 4.2 Convenciones para **crear commits**

#### **Formato:**
```
[codigo-jira] [tipo]: [descripción específica de la acción]
```

#### **Tipos de commit:**
- `feat`: Nueva funcionalidad
- `fix`: Corrección de errores
- `docs`: Cambios en documentación
- `style`: Cambios de formato/estilo (espacios, punto y coma, etc.)
- `refactor`: Refactorización de código sin cambios funcionales
- `test`: Agregar o modificar tests
- `chore`: Tareas de mantenimiento, configuración, dependencias

#### **Ejemplos de commits específicos:**
```bash
# ✅ BUENOS EJEMPLOS
git commit -m "26-feat: agregar validación de email en formulario login"
git commit -m "24-fix: corregir error de navegación en header mobile"


# ❌ EVITAR 
git commit -m "23-feat: agregar login"
git commit -m "24-fix: arreglar bug"

```

#### **Reglas para commits específicos:**
1. **Un commit = Una acción específica**: Cada commit debe representar un cambio lógico y completo
2. **Máximo 72 caracteres**: Para que sea legible en todas las herramientas Git
3. **Usar imperativo**: "agregar", "corregir", "actualizar" (no "agregado", "corrigiendo")
4. **Ser descriptivo**: Especificar QUÉ se cambió y DÓNDE
5. **Commits frecuentes**: Mejor muchos commits pequeños que pocos grandes

#### **Beneficios de commits específicos:**
- 🔄 **Rollback preciso**: Poder revertir solo la parte problemática
- 🔍 **Debugging eficiente**: Identificar rápidamente cuándo se introdujo un bug
- 📖 **Historial legible**: Entender la evolución del código
- 🤝 **Colaboración mejorada**: Reviews más fáciles y claras


---


## 5. ⚙️Tecnologías utilizadas

El backend del sistema ECIEXPRESS fue desarrollado con una arquitectura basada en **Spring Boot** y componentes del ecosistema **Java**, garantizando modularidad, mantenibilidad, seguridad y facilidad de despliegue.  
A continuación se detallan las principales tecnologías empleadas en el proyecto:

| **Tecnología / Herramienta** | **Versión / Framework** | **Uso principal en el proyecto** |
|------------------------------|--------------------------|----------------------------------|
| **Java OpenJDK** | 17 | Lenguaje de programación base del backend, orientado a objetos y multiplataforma. |
| **Spring Boot** | 3.x | Framework principal para la creación del API REST, manejo de dependencias e inyección de componentes. |
| **Spring Web** | — | Implementación del modelo MVC y exposición de endpoints REST. |
| **Spring Security** | — | Configuración de autenticación y autorización de usuarios mediante roles y validación de credenciales. |
| **Spring Data MongoDB** | — | Integración con la base de datos NoSQL MongoDB mediante el patrón Repository. |
| **MongoDB Atlas** | 6.x | Base de datos NoSQL en la nube utilizada para almacenar las entidades del sistema. |
| **Apache Maven** | 3.9.x | Gestión de dependencias, empaquetado del proyecto y automatización de builds. |
| **Lombok** | — | Reducción de código repetitivo con anotaciones como `@Getter`, `@Setter`, `@Builder` y `@AllArgsConstructor`. |
| **JUnit 5** | — | Framework para pruebas unitarias que garantiza el correcto funcionamiento de los servicios. |
| **Mockito** | — | Simulación de dependencias para pruebas unitarias sin requerir acceso a la base de datos real. |
| **JaCoCo** | — | Generación de reportes de cobertura de código para evaluar la efectividad de las pruebas. |
| **SonarQube** | — | Análisis estático del código fuente y control de calidad para detectar vulnerabilidades y malas prácticas. |
| **Swagger (OpenAPI 3)** | — | Generación automática de documentación y prueba interactiva de los endpoints REST. |
| **Postman** | — | Entorno de pruebas de la API, utilizado para validar respuestas en formato JSON con los métodos `POST`, `GET`, `PATCH` y `DELETE`. |
| **Docker** | — | Contenerización del servicio para garantizar despliegues consistentes en distintos entornos. |
| **Azure App Service** | — | Entorno de ejecución en la nube para el despliegue automático del backend. |
| **Azure DevOps** | — | Plataforma para la gestión ágil del proyecto, seguimiento de tareas y control de versiones. |
| **GitHub Actions** | — | Configuración de pipelines de integración y despliegue continuo (CI/CD). |
| **SSL / HTTPS** | — | Implementación de certificados digitales para asegurar la comunicación entre cliente y servidor. |

> 🧠 Estas tecnologías fueron seleccionadas para asegurar **escalabilidad**, **modularidad**, **seguridad**, **trazabilidad** y **mantenibilidad** del sistema, aplicando buenas prácticas de ingeniería de software y estándares de desarrollo moderno.


## 6. 🧩 Funcionalidad


Responsabilidad Principal: Gestionar la logística temporal y espacial de los pedidos: la programación de la recogida.

Funciones Clave:

Gestionar y exponer los horarios de operación configurados para cada punto de venta (consultados desde el módulo de Administración).

Validar y reservar franjas horarias para la recogida de pedidos, considerando la capacidad operativa del punto de venta.

Calcular y proporcionar los tiempos estimados de preparación para cada pedido, basándose en la complejidad de los ítems y la carga actual del punto de venta (posiblemente consultando al módulo statistics).

Actuar como el "reloj" del sistema para este módulo, asegurando que los pedidos se programen dentro de ventanas válidas y eficientes.


## 7. 📊 Diagramas

### Diagrama de clases

![img.png](docs/DiagramaClases.png)

### Diagrama Base de Datos

![img.png](docs/DiagramaBaseDatos.png)


### Diagrama de Componentes Especificos

![img.png](docs/DiagramaComponentesEspecificos.png)

## 8. 🌐 Endpoints expuestos y su información de entrada y salida

# Endpoints del Schedule Controller

## Categorías de Horarios

### 1. Crear Categoría de Horario
**Endpoint:** `POST /api/schedule/categories`

**Entrada (Body):**
```json
{
  "categoryName": "string",
  "startTime": "HH:mm:ss",
  "endTime": "HH:mm:ss"
}
```

**Salida:**
```json
{
  "id": "string",
  "categoryName": "string",
  "startTime": "HH:mm:ss",
  "endTime": "HH:mm:ss",
  "active": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### 2. Actualizar Categoría de Horario
**Endpoint:** `PUT /api/schedule/categories/{id}`

**Entrada (Body):** Misma estructura que crear categoría

**Salida:** Misma estructura que crear categoría

### 3. Cambiar Estado de Categoría
**Endpoint:** `PATCH /api/schedule/categories/{id}/status`

**Parámetros Query:**
- `active`: boolean (true/false)

**Salida:** Misma estructura que crear categoría

### 4. Obtener Todas las Categorías
**Endpoint:** `GET /api/schedule/categories`

**Salida:** Lista de categorías

### 5. Obtener Categorías Activas
**Endpoint:** `GET /api/schedule/categories/active`

**Salida:** Lista de categorías activas

### 6. Obtener Categoría por Nombre
**Endpoint:** `GET /api/schedule/categories/{categoryName}`

**Salida:** Detalle de la categoría

### 7. Verificar Estado de Categoría
**Endpoint:** `GET /api/schedule/categories/{categoryName}/status`

**Salida:**
```json
{
  "active": "boolean"
}
```

### 8. Eliminar Categoría
**Endpoint:** `DELETE /api/schedule/categories/{id}`

---

## Validación de Disponibilidad

### 9. Verificar Disponibilidad
**Endpoint:** `POST /api/schedule/availability`

**Entrada (Body):**
```json
{
  "pointOfSaleId": "string",
  "requestedTime": "datetime",
  "productCategory": "string"
}
```

**Salida:**
```json
{
  "available": "boolean",
  "message": "string",
  "suggestedTimes": ["datetime"],
  "alternativeSlots": "/* TimeSlotResponse */"
}
```

### 10. Verificar Disponibilidad con Sugerencias
**Endpoint:** `POST /api/schedule/availability/with-suggestions`

**Entrada (Body):** Misma estructura que verificar disponibilidad

**Salida:** Misma estructura que verificar disponibilidad

### 11. Validar Disponibilidad de Orden
**Endpoint:** `POST /api/schedule/availability/order`

**Entrada (Body):**
```json
{
  "pointOfSaleId": "string",
  "requestedTime": "datetime",
  "productCategories": ["string"]
}
```

**Salida:** Misma estructura que verificar disponibilidad

---

## Time Slots

### 12. Obtener Time Slots Disponibles
**Endpoint:** `GET /api/schedule/time-slots/{pointOfSaleId}`

**Parámetros Query:**
- `date`: "yyyy-MM-dd"

**Salida:** Lista de TimeSlotResponse

### 13. Obtener Time Slots con Filtros
**Endpoint:** `GET /api/schedule/time-slots/available`

**Parámetros Query:**
- `pointOfSaleId`: string
- `date`: "yyyy-MM-dd"
- `minCapacity`: integer (opcional)
- `productCategory`: string (opcional)

**Salida:**
```json
{
  "message": "string",
  "data": "/* TimeSlotResponse */",
  "timestamp": "datetime"
}
```

### 14. Obtener Time Slots para Ahora
**Endpoint:** `GET /api/schedule/time-slots/{pointOfSaleId}/now`

**Salida:**
```json
{
  "message": "string",
  "data": "/* TimeSlotResponse */",
  "timestamp": "datetime"
}
```

### 15. Validar Time Slots con Cierres
**Endpoint:** `GET /api/schedule/time-slots/{pointOfSaleId}/closures-validation`

**Parámetros Query:**
- `date`: "yyyy-MM-dd"

**Salida:**
```json
{
  "message": "string",
  "data": "/* TimeSlotResponse */",
  "timestamp": "datetime"
}
```

### 16. Crear Time Slot
**Endpoint:** `POST /api/schedule/time-slots`

**Entrada (Body):**
```json
{
  "pointOfSaleId": "string",
  "startTime": "datetime",
  "endTime": "datetime",
  "availableCapacity": "integer"
}
```

**Salida:**
```json
{
  "message": "string",
  "data": "/* TimeSlotResponse */",
  "timestamp": "datetime"
}
```

### 17. Generar Time Slots
**Endpoint:** `POST /api/schedule/time-slots/generate`

**Entrada (Body):**
```json
{
  "pointOfSaleId": "string",
  "date": "yyyy-MM-dd",
  "slotDurationMinutes": "integer",
  "defaultCapacity": "integer"
}
```

**Salida:**
```json
{
  "message": "string",
  "data": "/* TimeSlotResponse */",
  "timestamp": "datetime"
}
```

### 18. Reservar Time Slot
**Endpoint:** `POST /api/schedule/time-slots/{slotId}/reserve`

**Entrada (Body):**
```json
{
  "orderId": "string",
  "userId": "string"
}
```

**Salida:**
```json
{
  "message": "string",
  "data": "/* TimeSlotResponse *",
  "timestamp": "datetime"
}
```

### 19. Liberar Time Slot
**Endpoint:** `POST /api/schedule/time-slots/{slotId}/release`

**Entrada (Body):**
```json
{
  "orderId": "string"
}
```

**Salida:**
```json
{
  "message": "string",
  "data": "/* TimeSlotResponse */",
  "timestamp": "datetime"
}
```

---

## Horarios de Operación

### 20. Crear Horario de Operación
**Endpoint:** `POST /api/schedule/operating-hours`

**Entrada (Body):**
```json
{
  "pointOfSaleId": "string",
  "dayOfWeek": "integer (1-7)",
  "openingTime": "HH:mm:ss",
  "closingTime": "HH:mm:ss"
}
```

**Salida:**
```json
{
  "id": "string",
  "pointOfSaleId": "string",
  "dayOfWeek": "integer",
  "openingTime": "HH:mm:ss",
  "closingTime": "HH:mm:ss",
  "active": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### 21. Actualizar Horario de Operación
**Endpoint:** `PUT /api/schedule/operating-hours/{id}`

**Entrada (Body):** Misma estructura que crear horario

**Salida:** Misma estructura que crear horario

### 22. Cambiar Estado de Horario
**Endpoint:** `PATCH /api/schedule/operating-hours/{id}/status`

**Parámetros Query:**
- `active`: boolean

**Salida:** Misma estructura que crear horario

### 23. Obtener Todos los Horarios
**Endpoint:** `GET /api/schedule/operating-hours`

**Salida:** Lista de horarios

### 24. Obtener Horarios Activos por Punto de Venta
**Endpoint:** `GET /api/schedule/operating-hours/{pointOfSaleId}/active`

**Salida:** Lista de horarios activos

### 25. Obtener Todos los Horarios Activos
**Endpoint:** `GET /api/schedule/operating-hours/active`

**Salida:** Lista de horarios activos

---

## Cierres Temporales

### 26. Crear Cierre Temporal
**Endpoint:** `POST /api/schedule/temporary-closures`

**Entrada (Body):**
```json
{
  "pointOfSaleId": "string",
  "startDateTime": "datetime",
  "endDateTime": "datetime",
  "reason": "string"
}
```

**Salida:**
```json
{
  "id": "string",
  "pointOfSaleId": "string",
  "startDateTime": "datetime",
  "endDateTime": "datetime",
  "reason": "string",
  "active": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### 27. Obtener Cierres por Punto de Venta
**Endpoint:** `GET /api/schedule/temporary-closures/{pointOfSaleId}`

**Salida:** Lista de cierres

### 28. Obtener Todos los Cierres
**Endpoint:** `GET /api/schedule/temporary-closures`

**Salida:** Lista de cierres

### 29. Obtener Cierres Activos en Rango
**Endpoint:** `GET /api/schedule/temporary-closures/active/range`

**Parámetros Query:**
- `start`: "datetime"
- `end`: "datetime"

**Salida:** Lista de cierres activos

### 30. Actualizar Cierre Temporal
**Endpoint:** `PUT /api/schedule/temporary-closures/{id}`

**Entrada (Body):** Misma estructura que crear cierre

**Salida:** Misma estructura que crear cierre

### 31. Cambiar Estado de Cierre
**Endpoint:** `PATCH /api/schedule/temporary-closures/{id}/status`

**Parámetros Query:**
- `active`: boolean

**Salida:** Misma estructura que crear cierre

### 32. Eliminar Cierre Temporal
**Endpoint:** `DELETE /api/schedule/temporary-closures/{id}`

---

## Reportes

### 33. Reporte de Punto de Venta
**Endpoint:** `GET /api/schedule/reports/{pointOfSaleId}`

**Salida:** Datos del reporte

### 34. Reporte de Disponibilidad
**Endpoint:** `GET /api/schedule/reports/availability`

**Parámetros Query:**
- `start`: "datetime"
- `end`: "datetime"

**Salida:** Datos del reporte

### 35. Reporte de Categorías
**Endpoint:** `GET /api/schedule/reports/categories`

**Salida:** Datos del reporte

### 36. Reporte de Ocupación de Time Slots
**Endpoint:** `GET /api/schedule/reports/time-slots/occupancy`

**Parámetros Query:**
- `pointOfSaleId`: string
- `startDate`: "yyyy-MM-dd"
- `endDate`: "yyyy-MM-dd"

**Salida:**
```json
{
  "message": "string",
  "data": {
    "reportType": "string",
    "data": "datos del reporte", 
    "generatedAt": "datetime"
  },
  "timestamp": "datetime"
}
```

### 37. Reporte de Horas Pico
**Endpoint:** `GET /api/schedule/reports/time-slots/peak-hours`

**Parámetros Query:**
- `pointOfSaleId`: string
- `date`: "yyyy-MM-dd"

**Salida:** Misma estructura que reporte de ocupación

### 38. Reporte de Utilización de Capacidad
**Endpoint:** `GET /api/schedule/reports/time-slots/capacity-utilization`

**Parámetros Query:**
- `pointOfSaleId`: string

**Salida:** Misma estructura que reporte de ocupación

---

## Modelos de Respuesta Comunes

### TimeSlotResponse
```json
{
  "id": "string",
  "pointOfSaleId": "string",
  "startTime": "datetime",
  "endTime": "datetime",
  "availableCapacity": "integer",
  "reservedCapacity": "integer",
  "totalCapacity": "integer",
  "status": "string",
  "category": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### SuccessResponse
```json
{
  "message": "string",
  "data": "/* objeto de datos */",
  "timestamp": "datetime"
}
```

### ScheduleReportResponse
```json
{
  "reportType": "string",
  "data": "/* datos específicos del reporte */",
  "generatedAt": "datetime"
}
```

---

## Notas Importantes

1. **Formato de Fechas:**
    - `datetime`: ISO 8601 (ej: "2024-01-15T10:30:00")
    - `date`: "yyyy-MM-dd"
    - `time`: "HH:mm:ss"

2. **Códigos de Estado:**
    - 200: OK
    - 201: Created
    - 400: Bad Request
    - 404: Not Found
    - 500: Internal Server Error

3. **Validaciones:**
    - Todos los comandos tienen validación interna
    - Los parámetros requeridos deben ser proporcionados
    - Las fechas deben ser válidas y coherentes

4. **Autenticación:**
    - Todos los endpoints requieren autenticación (no mostrado en los ejemplos)


## 9. ⚠️ Manejo de Errores

El backend de ECIEXPRESS implementa un **mecanismo centralizado de manejo de errores** que garantiza uniformidad, claridad y seguridad en todas las respuestas enviadas al cliente cuando ocurre un fallo.

Este sistema permite mantener una comunicación clara entre el backend y el frontend, asegurando que los mensajes de error sean legibles, útiles y coherentes, sin exponer información sensible del servidor.

---

### 🧠 Estrategia general de manejo de errores

El sistema utiliza una **clase global** que intercepta todas las excepciones lanzadas desde los controladores REST.  
A través de la anotación `@ControllerAdvice`, se centraliza el manejo de errores, evitando el uso repetitivo de bloques `try-catch` en cada endpoint.

Cada error se transforma en una respuesta **JSON estandarizada**, que mantiene un formato uniforme para todos los tipos de fallos.

**📋 Estructura del mensaje de error:**

```json
{
  "timestamp": "2025-10-28T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "La materia con ID AYPR no existe.",
  "path": "/api/subjects/AYPR"
}
```

---

### ⚙️ Global Exception Handler

El **Global Exception Handler** es una clase con la anotación `@RestControllerAdvice` que captura y maneja todas las excepciones del sistema.  
Utiliza métodos con `@ExceptionHandler` para procesar errores específicos y devolver una respuesta personalizada acorde al tipo de excepción.

**✨ Características principales:**

- ✅ **Centraliza** la captura de excepciones desde todos los controladores
- ✅ **Retorna mensajes JSON consistentes** con el mismo formato estructurado
- ✅ **Asigna códigos HTTP** según la naturaleza del error (400, 404, 409, 500, etc.)
- ✅ **Define mensajes descriptivos** que ayudan tanto al desarrollador como al usuario
- ✅ **Mantiene la aplicación limpia**, eliminando bloques try-catch redundantes
- ✅ **Mejora la trazabilidad** y facilita la depuración en los entornos de prueba y producción

**🔄 Ejemplo conceptual de funcionamiento:**

Cuando se lanza una excepción del tipo `EntityNotFoundException`, el handler la intercepta y genera automáticamente una respuesta como:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "La materia con ID AYPR no existe.",
  "path": "/api/subjects/AYPR"
}
```

---

### 🧩 Validaciones en DTOs

Además del manejo global de errores, el sistema utiliza **validaciones automáticas** sobre los DTOs (Data Transfer Objects) para garantizar que los datos que llegan al servidor cumplan con las reglas de negocio antes de ejecutar cualquier lógica.

Estas validaciones se implementan mediante las anotaciones de **Javax Validation** y **Hibernate Validator**, como `@NotBlank`, `@NotNull`, `@Email`, `@Min`, `@Max`, entre otras.

**📝 Ejemplo de DTO con validaciones:**

```
Se va a ir actualizando

```

Si alguno de los campos no cumple las validaciones, se lanza automáticamente una excepción del tipo `MethodArgumentNotValidException`.  
Esta es capturada por el **Global Exception Handler**, que devuelve una respuesta JSON estandarizada con el detalle del campo inválido.

**⚠️ Ejemplo de respuesta ante error de validación:**

```

Se actualizara cuando se creen las clases

```

> 💡 Gracias a este mecanismo, se asegura que las peticiones erróneas sean detectadas desde el inicio, reduciendo fallos en capas más profundas como servicios o repositorios.

---

### 📊 Tipos de errores manejados

La siguiente tabla resume los principales tipos de excepciones manejadas en el sistema, junto con su respectivo código HTTP y un ejemplo de mensaje retornado:

| **Excepción** | **Código HTTP** | **Descripción del error** | **Ejemplo de mensaje** |
|---------------|-----------------|---------------------------|------------------------|
| `IllegalArgumentException` | `400 Bad Request` | Parámetros inválidos o peticiones mal estructuradas | *"El campo 'subjectId' no puede ser nulo."* |
| `MethodArgumentNotValidException` | `400 Bad Request` | Error de validación en un DTO o parámetro de entrada | *"El correo electrónico no cumple el formato válido."* |
| `EntityNotFoundException` | `404 Not Found` | El recurso solicitado no existe en la base de datos | *"La materia con ID AYPR no existe."* |
| `DuplicateKeyException` | `409 Conflict` | Intento de crear un registro que ya existe en MongoDB | *"El usuario ya se encuentra registrado."* |
| `AccessDeniedException` | `403 Forbidden` | Intento de acceder a un recurso sin permisos | *"Acceso denegado para el rol STUDENT."* |
| `Exception` | `500 Internal Server Error` | Error interno no controlado del servidor | *"Error inesperado del servidor."* |

---

### ✅ Beneficios del manejo centralizado

| **Beneficio** | **Descripción** |
|---------------|-----------------|
| 🎯 **Uniformidad** | Todas las respuestas de error tienen el mismo formato JSON |
| 🔧 **Mantenibilidad** | Agregar nuevas excepciones no requiere modificar cada controlador |
| 🔒 **Seguridad** | Oculta los detalles internos del servidor y evita exponer trazas del sistema |
| 📍 **Trazabilidad** | Cada error incluye información contextual (ruta y hora exacta) |
| 🤝 **Integración fluida** | Facilita la comunicación con el frontend y herramientas como Postman o Swagger |

---

> Gracias a este enfoque, el backend de ECIEXPRESS logra un manejo de errores **robusto**, **escalable** y **seguro**, garantizando una experiencia de usuario más confiable y profesional.

---


---

## 10. 🧪 Evidencia de las pruebas y cómo ejecutarlas


## 1. Compilar el Proyecto
```bash
mvn clean compile
```
**Propósito:** Compila todo el código fuente del proyecto

**Salida esperada:**
```text
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

## 2. Ejecutar Todas las Pruebas y Generar Reportes
```bash
mvn clean verify
```
**Propósito:**
- Limpia el proyecto
- Compila el código
- Ejecuta pruebas unitarias
- Ejecuta pruebas de integración (si existen)
- Genera reportes en `target/site/`

**Salida esperada:**
```text
[INFO] Tests run: XX, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- maven-site-plugin:3.12.1:site (default-site) @ KAPPA_OperationSchedule_BackEnd ---
[INFO] Generating "Surefire Report" report.
[INFO] Generating "JaCoCo" report.
[INFO] BUILD SUCCESS
```

## 📊 Estructura de Reportes Generados
Después de ejecutar `mvn clean verify`, se generan los siguientes reportes en `target/site/`:

```text
target/site/
├── surefire-report.html          # Reporte principal de pruebas
├── jacoco/                       # Cobertura de código
│   ├── index.html
│   ├── jacoco-resources/
│   └── ...
├── project-reports.html          # Reportes del proyecto
├── dependencies.html             # Dependencias del proyecto
├── plugin-management.html        # Gestión de plugins
└── ...
```

## 🔍 Verificación de los Reportes

### 2. Verificar Cobertura de Código
```bash
# En Windows
start target/site/jacoco/index.html
```

## 11. 🗂️ Código de la implementación organizado en las respectivas carpetas


**Estructura Hexagonal:**
- `Domain.Model/` → Entidades de negocio (puras)
- `Application/` → Casos de uso y comandos
- `Infrastructure/` → Repositorios y adaptadores
- `Web/` → Controladores y DTOs
- `Config/` → Configuraciones Spring
- `Exception/` → Manejo de errores

**Flujo:** Web → Application → Domain ← Infrastructure

---

## 12. 📝 Código documentado

**Usamos JavaDoc estándar:**

```java
/**
 * Descripción del propósito
 * @param nombre - descripción
 * @return qué retorna
 * @throws tipo - cuándo
 */
```

**Por capa:**
- **Dominio:** Reglas de negocio e invariantes
- **Aplicación:** Pre/Post condiciones de casos de uso
- **Web:** Endpoints con ejemplos JSON
- **Infraestructura:** Detalles de implementación

**Anotaciones comunes:**
- `@apiNote` para consumidores API
- `@implSpec` para desarrolladores
- `@deprecated` con alternativas




## 13. 🧾 Pruebas coherentes con el porcentaje de cobertura expuesto

![img.png](docs/pruebaCobertura.png)

**Cobertura Actual: 92%**
- **Domain Model:** 85% (mejorable)
- **Web Controller:** 99% (excelente)
- **Total:** 92% (meta cumplida)

**Estrategia de Pruebas Implementada:**
- ✅ **Controller:** Pruebas de integración con MockMvc (99%)
- ✅ **Use Cases:** Pruebas unitarias con Mockito
- ✅ **Domain:** Validaciones y lógica de negocio (85%)
- ⚠️ **Repositories:** Pruebas con @DataJpaTest
- ✅ **Exceptions:** Handlers probados


## 14. 🚀 Ejecución del Proyecto

**2. Compilar y Ejecutar:**
```bash
# Opción 1: Con Maven
mvn clean spring-boot:run

# Opción 2: Jar ejecutable
mvn clean package
java -jar target/KAPPA-OperationSchedule-BackEnd-1.0.0.jar

```

### Compilación en Swagger

![img.png](docs/EjecucionSwagger.png)

## 15. ☁️ Evidencia de CI/CD y Despliegue en Azure

![img.png](docs/ci-cd.png)

#### **📊 GitHub Actions Pipeline**

**Workflows Configurados:**
1. **CD - Deploy Schedule Backend (Dev)**
    - Despliega a ambiente de desarrollo
    - Se ejecuta automáticamente en push a develop
    - Estado: ✅ Ejecuciones exitosas visibles

2. **Deploy Schedule Backend (Prod)**
    - Despliega a ambiente de producción
    - Requiere aprobación manual
    - Estado: Tests and Quality (Cancelled) - necesita configuración
   
**Link de Despliegue en Azure: https://ecixpress.slack.com/archives/D09RJ13VAD9/p1764694232171399**



## 16. 🤝 Contribuciones y agradecimientos

El desarrollo del backend de ECIEXPRESS se realizó aplicando la **metodología ágil Scrum**, promoviendo la colaboración, la mejora continua y la entrega incremental de valor.  
Durante el proceso, el equipo KAPPA trabajó en **sprints semanales**, realizando **revisiones de avance**, **dailies** y **retrospectivas**, lo que permitió mantener una comunicación fluida y adaptarse a los cambios de requisitos en tiempo real.

Cada miembro del equipo asumió un rol dentro del marco de Scrum:

| **Rol Scrum** | **Responsabilidad principal** |
|----------------|-------------------------------|
| **Product Owner** | Definir y priorizar las historias de usuario en el backlog del producto. |
| **Scrum Master** | Asegurar la aplicación de la metodología y eliminar impedimentos. |
| **Developers** | Diseñar, implementar, probar y documentar las funcionalidades. |

**Artefactos y eventos Scrum utilizados:**
- 📋 **Product Backlog:** listado de funcionalidades priorizadas y mantenidas en Jira/GitHub Projects.
- 🧩 **Sprint Backlog:** tareas seleccionadas por sprint según la capacidad del equipo.
- ⚙️ **Daily Scrum:** reuniones cortas de sincronización para identificar bloqueos y avances.
- 📦 **Sprint Review:** revisión de resultados y demostración del incremento funcional.
- 🔄 **Sprint Retrospective:** análisis de mejoras en la dinámica y la comunicación del equipo.

> 💡 Gracias al uso de Scrum, el desarrollo de KAPPA se mantuvo **organizado, transparente y enfocado en la entrega continua de valor**, aplicando principios de autoorganización y aprendizaje colaborativo.

## 🤝 Contribuciones y mantenimiento

**Desarrollado por el equipo KAPPA – DOSW 2025-2**

### 🙌 ¡Gracias por visitar ECIEXPRESS!

- Si tienes sugerencias, encuentras errores o deseas aportar nuevas funcionalidades, ¡las contribuciones son bienvenidas!
- Puedes abrir un **issue** o enviar un **pull request** siguiendo las buenas prácticas de colaboración del repositorio.

> 💡 **ECIEXPRESS** es un proyecto académico, pero su arquitectura y calidad están pensadas para ser escalables y adaptables a escenarios reales en instituciones educativas.

---

### 🚀 ECIEXPRES nació como una idea para optimizar los procesos académicos y terminó convirtiéndose en un proyecto que combina tecnología, trabajo en equipo y propósito!

---

