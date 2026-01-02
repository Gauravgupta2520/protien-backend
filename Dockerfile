FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the entire protien directory
COPY protien/ ./protien/

# Make Maven wrapper executable
RUN chmod +x ./protien/mvnw

# Build the application
RUN cd protien && ./mvnw clean package -DskipTests

EXPOSE 8080

# Run the application
CMD ["sh", "-c", "cd protien && java -jar target/protien-0.0.1-SNAPSHOT.jar"]
