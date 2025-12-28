# 1️⃣ Base image: Java 21 installed
FROM eclipse-temurin:21-jdk

# 2️⃣ Create working directory inside container
WORKDIR /app

# 3️⃣ Copy Maven wrapper & pom.xml
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# 4️⃣ Download dependencies (faster builds later)
RUN ./mvnw dependency:go-offline

# 5️⃣ Copy source code
COPY src src

# 6️⃣ Build Spring Boot JAR
RUN ./mvnw clean package -DskipTests

# 7️⃣ Expose port (Render will override this)
EXPOSE 8080

# 8️⃣ Run the application
CMD ["java", "-jar", "target/auth-service-0.0.1-SNAPSHOT.jar"]