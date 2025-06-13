# First build stage (Maven)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Second stage (run the built JAR)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
