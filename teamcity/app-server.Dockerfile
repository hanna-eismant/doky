FROM openjdk:17

COPY --from=datadog/serverless-init:1 /datadog-init /app/datadog-init
ADD 'https://dtdg.co/latest-java-tracer' /dd_tracer/java/dd-java-agent.jar

ARG DD_VERSION=Aardvark-v0.1

ENV DD_SERVICE=app-server
ENV DD_ENV=prod
ENV DD_VERSION=$DD_VERSION

ARG DD_GIT_REPOSITORY_URL
ARG DD_GIT_COMMIT_SHA
ENV DD_GIT_REPOSITORY_URL=${DD_GIT_REPOSITORY_URL}
ENV DD_GIT_COMMIT_SHA=${DD_GIT_COMMIT_SHA}

EXPOSE 8080

ARG JAR_FILE=app-server/build/libs/app-server.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["/app/datadog-init"]

CMD ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]
