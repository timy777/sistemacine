# Dockerfile que CONSTRUYE todo y muestra errores claramente
FROM eclipse-temurin:17-jdk-jammy

# Instalar Node.js (con verificación)
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    echo "Node version: $(node --version)" && \
    echo "NPM version: $(npm --version)"

# Configurar trabajo
WORKDIR /app
COPY . .

# Construir frontend (con verificación)
RUN echo "=== CONSTRUYENDO FRONTEND ===" && \
    npm install && \
    npm run webapp:build && \
    echo "=== FRONTEND CONSTRUIDO ==="

# Construir backend (con verificación)
RUN echo "=== CONSTRUYENDO BACKEND ===" && \
    ./mvnw clean package -DskipTests && \
    echo "=== BACKEND CONSTRUIDO ===" && \
    echo "Archivos en target:" && \
    ls -la target/

# Verificar que el JAR existe
RUN JAR_FILE=$(ls target/*.jar) && \
    echo "JAR encontrado: $JAR_FILE" && \
    cp "$JAR_FILE" app.jar

# Configuración
ENV SPRING_PROFILES_ACTIVE=dev
EXPOSE 8083

# Usar el JAR específico
ENTRYPOINT ["java","-jar","app.jar"]