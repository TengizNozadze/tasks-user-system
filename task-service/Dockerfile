FROM maven:3.8.8-eclipse-temurin-18 AS build
WORKDIR /app
COPY pom.xml ./
COPY task-service/pom.xml ./
COPY . ./
RUN mvn -f task-service/pom.xml -DskipTests package

FROM eclipse-temurin:18-jre
WORKDIR /app
COPY --from=build /app/task-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]

