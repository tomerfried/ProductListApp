FROM openjdk:17-jdk-alpine

ARG JAR_FILE=products_manager-0.0.1-SNAPSHOT.jar

COPY ./build/libs/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]