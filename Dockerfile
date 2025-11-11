# ========================================
# ?? Dockerfile - JHipster (modo desarrollo)
# ========================================

# Imagen base (Java 17 JDK)
FROM eclipse-temurin:17-jdk-jammy

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar solo el archivo pom.xml primero (mejor caching)
COPY pom.xml .

# Instalar Maven y descargar dependencias
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline -B

# Copiar el resto del proyecto
COPY . .

# Puerto interno de la aplicación (según application-dev.yml)
EXPOSE 8083

# Perfil de Spring Boot (modo desarrollo)
ENV SPRING_PROFILES_ACTIVE=dev

# Variables opcionales de conexión a MongoDB (sin usuario ni contraseña)
ENV SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/
ENV SPRING_DATA_MONGODB_DATABASE=sistemacine

# Comando por defecto: ejecutar en modo dev con Maven
CMD ["mvn", "spring-boot:run"]
