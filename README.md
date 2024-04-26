[![tests](https://github.com/hanna-eismant/doky/actions/workflows/gradle-main.yml/badge.svg)](https://github.com/hanna-eismant/doky/actions/workflows/gradle-main.yml)
![GitHub License](https://img.shields.io/github/license/hanna-eismant/doky)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://hanna-eismant.github.io/doky/javadoc/)
    

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

# Technical Information

## DEV environment

To development process there is an DEvV environment that is hosted on Azure

1. Backend - https://doky-dev.azurewebsites.net/
   ![Website](https://img.shields.io/website?url=https%3A%2F%2Fdoky-dev.azurewebsites.net) 
2. Frontend - https://doky-dev-front.azurewebsites.net/
   ![Website](https://img.shields.io/website?url=https%3A%2F%2Fdoky-dev-front.azurewebsites.net)

## Backend
Tech Stack:
1. **MySql**: A reliable relational database management system (RDBMS) for structured data storage.
2. **Spring Boot**: The backbone of Doky’s backend infrastructure, enabling rapid development and simplified 
   configuration.
3. **Apache Solr**: A powerful, open-source search platform built on Apache Lucene, offering full-text search, 
   faceted search, and distributed capabilities. To configure Solr separate project is created - 
   https://github.com/hanna-eismant/doky-solr
                                                    
## Frontend
As project has RESTfull API, the frontend has a separate project - https://github.com/hanna-eismant/doky-front
