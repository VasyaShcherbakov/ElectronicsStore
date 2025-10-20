# ===== Этап 1: сборка приложения =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Скопировать pom.xml и исходники
COPY pom.xml .
COPY src ./src

# Собрать jar (без тестов, быстрее)
RUN mvn clean package -DskipTests

# ===== Этап 2: запуск =====
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Скопировать собранный jar из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

# Порт, который слушает Spring Boot
EXPOSE 8080

# Переменные среды (могут быть переопределены docker-compose)
ENV DB_URL=jdbc:postgresql://localhost:5432/postgres
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
