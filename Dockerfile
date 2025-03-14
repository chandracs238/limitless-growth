# Use an official OpenJDK base image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy Maven Wrapper and permission fix
COPY mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw

# Copy the project files
COPY pom.xml ./
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080 (or change if needed)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/*.jar"]
