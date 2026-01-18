FROM maven:3.8.8-eclipse-temurin-18 AS build
WORKDIR /app
COPY pom.xml ./
COPY auth-service/pom.xml ./
COPY . ./
RUN mvn -f auth-service/pom.xml -DskipTests package

FROM eclipse-temurin:18-jre
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

