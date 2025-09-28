# Build stage
FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test

# Package stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
