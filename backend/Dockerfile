FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Копіюємо файли (оскільки ми вже в контексті backend, шляхи прямі)
COPY pom.xml .
COPY src ./src

# Збірка
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Копіюємо зібраний jar
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Обов'язково додаємо профіль (якщо у вас є окремий для prod)
ENTRYPOINT ["java", "-jar", "app.jar"]
