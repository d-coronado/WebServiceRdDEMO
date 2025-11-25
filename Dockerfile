# 1. Etapa de construcción
# Imagen base con Eclipse Temurin JDK 17
FROM eclipse-temurin:17-alpine AS build

LABEL author=Davis-Coronado

# Definir directorio raiz de nuestra contenedor
WORKDIR /app

# Copiar el Maven Wrapper y pom.xml
COPY ./pom.xml /app
COPY ./.mvn /app/.mvn
COPY ./mvnw /app

# Dar permisos de ejecución al wrapper
RUN chmod +x mvnw

# Descargar dependencias de Maven (mejor cache)
RUN ./mvnw dependency:go-offline

# Copiar el codigo fuente al contenedor
COPY ./src /app/src

# Construir el proyecto
RUN ./mvnw clean package

# 2. Etapa de runtime
# Imagen base con Eclipse Temurin JRE 17, mas liviano para correr la aplicacion
FROM eclipse-temurin:17-jre-alpine

LABEL author=Davis-Coronado

# Definir directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/WebServiceRDDemo-1.0-SNAPSHOT.jar /app/app.jar

# Informar el puerto donde se ejecuta el contenedor (informativo)
EXPOSE 8080

# Levantar nuestra aplicacion cuando el contenedor inicie
ENTRYPOINT ["java","-jar","/app/app.jar"]



