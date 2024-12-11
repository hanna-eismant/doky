FROM openjdk:17

EXPOSE 8080

ENV ENV_PROFILE=prod
ARG JAR_FILE=app-server/build/libs/app-server.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=${ENV_PROFILE}", "/app.jar"]
