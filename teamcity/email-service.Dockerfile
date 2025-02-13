FROM openjdk:17

COPY --from=datadog/serverless-init:latest /datadog-init /app/datadog-init
ADD 'https://dtdg.co/latest-java-tracer' /dd_tracer/java/dd-java-agent.jar

ARG DD_VERSION=Aardvark-v0.1
ARG DD_SERVICE=email-service
ARG DD_ENV=prod

ENV DD_SERVICE=$DD_SERVICE
ENV DD_ENV=$DD_ENV
ENV DD_VERSION=$DD_VERSION

LABEL com.datadoghq.tags.service=$DD_SERVICE
LABEL com.datadoghq.tags.env=$DD_ENV
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

ARG JAR_FILE=email-service/build/libs/email-service.jar
COPY ${JAR_FILE} app.jar

CMD ["/app/datadog-init", "java", "-jar", "-Dspring.profiles.active=$DD_ENV", "/app.jar"]
