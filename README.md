# Custom Project Backend

Back-end application for Custom project.  
The JDK is required to run this application.  
Please complete the JDK set-up first.

## Requirement

- [JDK](https://openjdk.org/): Java SDK: ![](https://img.shields.io/badge/version-21.0.5-blue)

- [Maven](https://maven.apache.org/): [org.springframework.boot](https://plugins.gradle.org/plugin/org.springframework.boot): ![](https://img.shields.io/badge/version-3.4.0-blue)

- [Docker](https://www.docker.com/): Container environment tools
- [Docker Compose](https://docs.docker.com/compose/): Docker plugins

## Getting Started

### 1. Build the Application

1. Build docker container

   ```shell
   docker build -t custom-service .
   ```

2. Run PostgresSQL container

   ```shell
   docker create --name ngow-postgres-container \
   -e POSTGRES_USER=custom \
   -e POSTGRES_PASSWORD=custom \
   -e POSTGRES_DB=custom \
   -p 54321:5432 \
   postgres:15
   ```

   ```shell
   docker start ngow-postgres-container
   ```

3. Run application container

   ```shell
   docker run -d \
   -e POSTGRES_URL=jdbc:postgresql://localhost:54321/custom \
   -e POSTGRES_USERNAME=custom \
   -e POSTGRES_PASSWORD=custom \
   --network host \
   custom-service:latest
   ```
