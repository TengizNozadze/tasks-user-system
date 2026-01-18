FROM maven:3.8.8-eclipse-temurin-18 AS build
WORKDIR /app
COPY pom.xml ./
COPY gateway-service/pom.xml ./
COPY . ./
RUN mvn -f gateway-service/pom.xml -DskipTests package

FROM eclipse-temurin:18-jre
WORKDIR /app
COPY --from=build /app/gateway-service/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/app/app.jar"]

