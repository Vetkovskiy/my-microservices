FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache git curl
WORKDIR /app
RUN git clone https://github.com/Vetkovskiy/config-repo.git /app/config-repo
COPY target/*.jar app.jar
EXPOSE 8889
ENTRYPOINT ["java", "-jar", "app.jar"]