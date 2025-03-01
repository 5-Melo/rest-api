FROM openjdk:21-jdk

# Set working directory
WORKDIR /app

# Copy all project files into the container
COPY . .

# Pass environment variables from build args
ARG MONGO_URI
ARG MONGO_DATABASE
ARG JWT_SECRET_KEY
ARG JWT_EXPIRATION_TIME
ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET

# Build JAR inside the container
RUN ./mvnw clean package -DskipTests

# Run the application
CMD ["java", "-jar", "target/MeloTech-0.0.1-SNAPSHOT.jar"]
