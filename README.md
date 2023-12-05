# Spring Boot 3 Starter

Spring Boot 3 Starter Project.

## 1. Technologies used

- [Java 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
- [Maven 3.9.4](https://maven.apache.org/download.cgi)
- [Spring Boot 3.1.2](https://spring.io/projects/spring-boot)
- [Docker](https://docs.docker.com/get-docker/)
- [Flyway](https://flywaydb.org/)
- [Postgresql](https://www.postgresql.org/)
- ...

## 2. Infrastructures

All infrastructures are being deployed as docker services. Follow the commands below to start them:

```bash
$ cd ./infrastructure

# local
$ docker-compose up -d
```

## 3. Modules

- `shared`: shared resources, such as CQRS interfaces, exceptions, utils and so on
- `domain`: entities and repositories
- `common`: initial resources
- `auth`, `user`: normal modules
- `web-api`: main application

## 4. Development

Building the project is dependent on the environment you want to build for. The default environment is "dev".
- Build: `mvn clean package` or `mvn clean package -DskipTests` if you want to skip unit tests for faster build. However, it's not recommended skipping the tests.
- Database Migration:

```bash
$ mvn flyway:migrate -P[env] -Dflyway.user=[user] -Dflyway.password=[password] -Dflyway.url=[url]
```

- Running the application: `mvn spring-boot:run` to start spring boot application on embedded server (dev env). The command has to be executed in the `web-api` module.

## 5. Deployment

- Install docker at https://docs.docker.com/get-docker/
- From root folder, run:

```bash
$ docker login --username xxx --password xxx

# dev
$ mvn clean install -Pdev -DskipTests
$ mvn deploy -Ddocker.skip=false -Pdev -f web-api/pom.xml
```

- Access to server, run:

```bash
# dev
$ docker stop springboot3starter
$ docker-compose pull
$ docker-compose up -d
```

## 6. Default users

- Administrator: `luan.tran@eztek.vn / QY3L7QufJS869qnZ`
