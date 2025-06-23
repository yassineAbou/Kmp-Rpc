# Stage 1: Build the JAR
FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/app
WORKDIR /home/gradle/app
# Build and include resources
RUN gradle :server:buildFatJar

# Stage 2: Runtime Image
FROM amazoncorretto:22 AS runtime
EXPOSE 8080
RUN mkdir -p /app
COPY --from=build /home/gradle/app/server/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]