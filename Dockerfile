# Stage 1: Build the application
FROM adoptopenjdk:21-jdk-hotspot AS build
WORKDIR /app
COPY . .
RUN ./gradlew build

# Stage 2: Create the final image
FROM adoptopenjdk:21-jre-hotspot
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
