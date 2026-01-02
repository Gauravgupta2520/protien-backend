FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml first for better caching
COPY protien/mvnw protien/mvnw
COPY protien/mvnw.cmd protien/mvnw.cmd
COPY protien/.mvn protien/.mvn
COPY protien/pom.xml protien/

# Download dependencies
RUN cd protien && ./mvnw dependency:go-offline -B

# Copy source code
COPY protien/src protien/src

# Build the application
RUN cd protien && ./mvnw clean package -DskipTests

EXPOSE 8080

# Run the application
CMD ["sh", "-c", "cd protien && java -jar target/protien-0.0.1-SNAPSHOT.jar"]
