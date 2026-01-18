# multi-stage build for user-service
FROM maven:3.8.8-eclipse-temurin-18 AS build
WORKDIR /app
COPY pom.xml ./
COPY user-service/pom.xml ./
# copy rest of project
COPY . ./
RUN mvn -f user-service/pom.xml -DskipTests package

FROM eclipse-temurin:18-jre
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]

