FROM openjdk:24-jdk-slim
WORKDIR /app

# Copy the JAR built by Maven into the container
COPY target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
