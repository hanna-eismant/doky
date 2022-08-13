FROM openjdk:11

EXPOSE 8118

ENV ENV_PROFILE=prod
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=$ENV_PROFILE", "/app.jar"]
