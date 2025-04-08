![GitHub License](https://img.shields.io/github/license/hanna-eismant/doky)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://hanna-eismant.github.io/doky/javadoc/)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hanna-eismant_doky&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=hanna-eismant_doky)
![TeamCity Build Status](https://hkurh-pets.teamcity.com/app/rest/builds/buildType:(id:Doky_BasicChecks_AggregateResult),branch:main/statusIcon)


> [!NOTE]
> This project is created for investigations and learning

# Description

## Doky: Open-Source Document Management

Doky is an open-source project designed to revolutionize document management. Whether you’re an individual, 
a team, or an organization, Doky provides a powerful platform for handling documents efficiently. 
Here’s what you need to know:

1. **Document Storage Made Easy**: Doky serves as a central repository for all your files. From text documents to 
   images, spreadsheets, and presentations, everything finds a home in Doky.
2. **Search and Retrieve**: Say goodbye to endless folder searches. Doky’s robust search functionality lets you
   find specific documents using keywords, tags, or metadata.
3. **Access Control**: Set permissions based on roles. Only authorized users can view, edit, or delete documents.
4. **Collaborate Seamlessly**: Work together in real time. Share files, leave comments, and collaborate effortlessly.

# Configuration

The application uses configuration files for its setup. You'll find the default properties in `application.properties`.
There is addition configuration `scheduler.properties` where are defined schedule for jobs.

#### Azure App Configuration

It is recommended to store sensitive values in Azure App Configuration. See configuration in `bootstrap.properties`. The
connection string to Azure App Configuration should be stored in environment variable
`AZURE_APP_CONFIG_CONNECTION_STRING`
