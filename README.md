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
   docker run -d --name ${{ secrets.CONTAINER_NAME }} \
            -p 6820:6820 ${{ secrets.DOCKER_USERNAME }}/${{ secrets.IMAGE_NAME }}:latest \
            -e ACTIVE_PROFILES=${{ secrets.ACTIVE_PROFILES }} \
            -e MYSQL_URL=${{ secrets.MYSQL_URL }} \
            -e MYSQL_USERNAME=${{ secrets.MYSQL_USERNAME }} \
            -e MYSQL_PASSWORD=${{ secrets.MYSQL_PASSWORD }}
   ```
