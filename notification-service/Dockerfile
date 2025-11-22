FROM maven:3.9.11-amazoncorretto-25-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app
COPY --from=build /app/target/notification-service.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
