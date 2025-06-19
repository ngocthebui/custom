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
2. Run docker

   ```shell
   docker create --name ngow-mysql-container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=custom -p 33066:3306 mysql:latest
   ```

   ```shell
   docker start ngow-mysql-container
   ```

   ```shell
   docker run -d \
   -e MYSQL_URL=jdbc:mysql://localhost:33066/custom \
   -e MYSQL_USERNAME=root \
   -e MYSQL_PASSWORD=root \
   --network host \
   custom-service:latest
   ```
