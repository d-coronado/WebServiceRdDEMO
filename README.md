# WebServiceRdDEMO

Proyecto Java con Spring Boot y Maven que implementa servicios web para interactuar con la DGII (República Dominicana).

## 📋 Descripción

Se aplicó **arquitectura hexagonal + vertical slice** para:
- Mejorar separación de responsabilidades
- Aplicar principios SOLID
- Facilitar escalabilidad y mantenimiento

### Para prueba rápida (recomendado)
- **Docker** y **Docker Compose** únicamente

> 💡 **Recomendación:** Si solo quieres probar el proyecto rápidamente, usa Docker. No necesitas instalar Java, Maven ni MySQL.
>
## 🐳 Docker

El proyecto incluye `Dockerfile` y `docker-compose.yml` para facilitar el despliegue.
```bash
# Construir y levantar contenedores
docker compose up --build

# Ejecutar en segundo plano
docker compose up -d

# Detener contenedores
docker compose down
```

La aplicación estará disponible en `http://localhost:8080` (por defecto).

## 📚 Documentación API

Una vez levantada la aplicación, puedes acceder a la documentación interactiva:

- **Swagger UI:** [http://localhost:8080/docs](http://localhost:8080/docs)

### Colección de Postman

Puedes importar la colección de Postman para probar los endpoints:

📁 **Archivo:** `src/main/resources/Demo-RD.postman_collection.json`

## ⚠️ Importante: Certificados DGII

Para probar los servicios de integración con la DGII se requiere un **certificado digital válido y autorizado** por un ente certificador reconocido por la DGII de República Dominicana.

**Si no cuentas con un certificado:**
- Los tests unitarios están diseñados para probar la funcionalidad **aislando las dependencias externas** (DGII).
- Puedes ejecutar `mvn test` para validar la lógica de negocio sin necesidad de certificados reales.

### Servicios que requieren certificado válido:
- **`FirmarDocumentByTenantUseCase`**: Firma digital de documentos XML
- **`CreateSesionUseCase`**: Creación y validación de sesiones con DGII, para firmar se necesita el certificado valido
- **`UploadCertificadoByTenantUseCase`**: Puedes simular la carga usando cualquier archivo con extensión `.p12` (no necesita ser un certificado válido para testing)

## 📦 Requisitos para desarrollo local

- **Java 17+**
- **Maven 3.6+**
- **MySQL 8.0+** (o compatible)
- **Docker** (opcional, para despliegue con contenedores)


## ⚙️ Variables de entorno

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DATABASE_URL` | URL de conexión a BD | `jdbc:mysql://localhost:3306/billing_rd_demo` |
| `DATABASE_USERNAME` | Usuario de BD | `root` |
| `DATABASE_PASSWORD` | Contraseña de BD | `tu_password` |
| `LOG_PATH` | Directorio base para logs de aplicación (debe existir) | `/Users/dcoronado/demo-rd/logs` |
| `FILESYSTEM_PATH` | Directorio base para crear el árbol de directorios | `/Users/dcoronado/demo-rd/` |

> **Nota:** Asegúrate de que las rutas definidas en `LOG_PATH` y `FILESYSTEM_PATH` existan en tu sistema antes de ejecutar la aplicación.

## 🗄️ Base de datos

1. Crear la base de datos:
```sql
CREATE DATABASE billing_rd_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Las migraciones se ejecutan automáticamente al iniciar la aplicación.

## 🚀 Cómo levantar el proyecto

### 1. Clonar el repositorio
```bash
git clone https://github.com/d-coronado/WebServiceRdDEMO.git
cd WebServiceRdDEMO
```

### 2. Configurar variables de entorno

**Opción A: Variables de sistema (Linux/Mac)**
```bash
export DATABASE_URL=jdbc:mysql://localhost:3306/billing_rd_demo
export DATABASE_USERNAME=root
export DATABASE_PASSWORD=tu_password
export LOG_PATH=/Users/dcoronado/demo-rd/logs
export FILESYSTEM_PATH=/Users/dcoronado/demo-rd/
```

**Opción B: Variables de sistema (Windows)**
```cmd
set DATABASE_URL=jdbc:mysql://localhost:3306/billing_rd_demo
set DATABASE_USERNAME=root
set DATABASE_PASSWORD=tu_password
set LOG_PATH=C:\demo-rd\logs
set FILESYSTEM_PATH=C:\demo-rd\
```

**Opción C: application.properties (recomendado para desarrollo local)**

Edita `src/main/resources/application.yml`:
```properties
spring.datasource.url=${DATABASE_URL:jdbc:mysql://localhost:3306/billing_rd_demo}
spring.datasource.username=${DATABASE_USERNAME:root}
spring.datasource.password=${DATABASE_PASSWORD:}
logging.path=${LOG_PATH:/tmp/logs}
app.filesystem.path=${FILESYSTEM_PATH:/tmp/demo-rd/}
```

**Opción D: Configuración en IDE**
- **IntelliJ IDEA:** Run → Edit Configurations → Environment Variables

**Opción E: Pasarlas al ejecutar Maven**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--DATABASE_URL=jdbc:mysql://localhost:3306/billing_rd_demo --DATABASE_USERNAME=root --DATABASE_PASSWORD=tu_password"
```

### 3. Ejecutar en local con Maven

**Compilar el proyecto:**
```bash
mvn clean package
```

**Ejecutar la aplicación:**
```bash
mvn spring-boot:run
```

**O ejecutar el JAR generado:**
```bash
java -jar target/*.jar
```

## 🧪 Pruebas

Ejecutar los tests:
```bash
mvn test
```

## 📁 Estructura del proyecto
```
WebServiceRdDEMO/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourpackage/
│   │   │       ├── tenant/      # Gestión de tenants
│   │   │       ├── sesion/      # Tokens DGII
│   │   │       ├── sing/        # Firma digital
│   │   │       ├── dgii/        # Consumo API DGII mediante RestClient
│   │   │       └── shared/      # Utilidades compartidas
│   │   └── resources/
│   │       ├── application.properties
│   │       └── logback-spring.xml  # Configuración de logs (rotación 7 días)
│   └── test/
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## 📝 Características técnicas

- **Arquitectura:** Hexagonal + Vertical Slice
- **HTTP Client:** RestClient (Spring 6+)
- **Logs:** Rotación automática a 7 días (configurado en `logback-spring.xml`)
- **Firma digital:** Integración con certificados para DGII
- **Base de datos:** MySQL con JPA/Hibernate


## 📧 Contacto

Davis Coronado - daviscoronadoalbines@gmail.com
