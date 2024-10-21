# Use an official Maven image to build the application
FROM maven:3.6.3-openjdk-11-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copies into the working directory
COPY pom.xml .
COPY src ./src
COPY webpack ./webpack
COPY sonar-project.properties .
COPY package.json .
COPY tsconfig.json .

# Build the application
RUN mvn clean package -DskipTests

# Use an OpenJDK image to run the Spring Boot application
FROM azul/zulu-openjdk:11-latest

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the Maven build container
COPY --from=build /app/target/*.jar /app/laris-assistant-app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Define the command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/laris-assistant-app.jar"]
