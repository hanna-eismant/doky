FROM mcr.microsoft.com/azure-functions/java:4-java17

COPY --from=datadog/serverless-init:1 /datadog-init /app/datadog-init
ADD 'https://dtdg.co/latest-java-tracer' /dd_tracer/java/dd-java-agent.jar

ARG DD_VERSION=Aardvark-v0.1

ENV DD_SERVICE=cleanup
ENV DD_ENV=prod
ENV DD_VERSION=$DD_VERSION

LABEL com.datadoghq.tags.service=cleanup
LABEL com.datadoghq.tags.env=prod
LABEL com.datadoghq.tags.version=$DD_VERSION

ENV DD_LOGS_ENABLED=true
ENV DD_IAST_ENABLED=true
ENV DD_APPSEC_ENABLED=true
ENV DD_PROFILING_ENABLED=true
ENV DD_TRACE_ENABLED=true
ENV DD_LOGS_INJECTION=true
ENV DD_TRACE_SAMPLE_RATE=1

WORKDIR /home/site/wwwroot

COPY cleanup-functions/build/azure-functions/cleanup-functions /home/site/wwwroot

CMD ["/app/datadog-init", "java", "-jar", "/azure-functions-host/workers/java/azure-functions-java-worker.jar" ]
