FROM openjdk:17

COPY --from=datadog/serverless-init:1 /datadog-init /app/datadog-init
ADD 'https://dtdg.co/latest-java-tracer' /dd_tracer/java/dd-java-agent.jar

ARG DD_VERSION=Aardvark-v0.1

ENV DD_SERVICE=app-server
ENV DD_ENV=prod
ENV DD_VERSION=$DD_VERSION

LABEL com.datadoghq.tags.service=app-server
LABEL com.datadoghq.tags.env=prod
LABEL com.datadoghq.tags.version=$DD_VERSION

ENV DD_TRACE_SQL_ENABLED=true
ENV DD_DATABASE_MONITORING=true
ENV DD_MSSQL_ENABLED=true

ENV DD_LOGS_ENABLED=true
ENV DD_IAST_ENABLED=true
ENV DD_APPSEC_ENABLED=true
ENV DD_PROFILING_ENABLED=true
ENV DD_TRACE_ENABLED=true
ENV DD_LOGS_INJECTION=true
ENV DD_TRACE_SAMPLE_RATE=1

EXPOSE 8080

ARG JAR_FILE=app-server/build/libs/app-server.jar
COPY ${JAR_FILE} app.jar

CMD ["/app/datadog-init", "java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
