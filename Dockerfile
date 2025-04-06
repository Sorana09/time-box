# Use an OpenJDK image from Docker Hub to run the application
FROM openjdk:24-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the generated .jar file from the target directory into the container
# Expose the port the app will run on
EXPOSE 8080

# Define the command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
