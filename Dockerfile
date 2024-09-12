# Imagen base de OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/app-1.0-jar-with-dependencies.jar /app/app-1.0.jar

# Comando de entrada
ENTRYPOINT ["java", "-jar", "/app/app-1.0.jar"]

# Exponer el puerto
EXPOSE 8080
