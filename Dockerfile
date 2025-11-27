FROM eclipse-temurin:21-jre

# Accept build argument
ARG OPENAI_API_KEY

# Set as environment variable
ENV OPENAI_API_KEY=${OPENAI_API_KEY}

# Accept build argument
ARG KAKAO_API_KEY

# Set as environment variable
ENV KAKAO_API_KEY=${KAKAO_API_KEY}

WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]