# jms-social-media

[![Build Status](https://travis-ci.org/JasonSarwar/jms-social-media.svg?branch=master)](https://travis-ci.org/JasonSarwar/jms-social-media) 
[![<Sonarcloud Quality Gate>](https://sonarcloud.io/api/project_badges/measure?project=JasonSarwar_jms-social-media&metric=alert_status)](https://sonarcloud.io/dashboard?id=JasonSarwar_jms-social-media) 
[![codecov](https://codecov.io/gh/JasonSarwar/jms-social-media/branch/master/graph/badge.svg)](https://codecov.io/gh/JasonSarwar/jms-social-media) 
[![GitHub version](https://badge.fury.io/gh/JasonSarwar%2Fjms-social-media.svg)](https://badge.fury.io/gh/JasonSarwar%2Fjms-social-media) 
[![Commit Activity](https://img.shields.io/github/commit-activity/m/jasonsarwar/jms-social-media.svg)](https://github.com/JasonSarwar/jms-social-media/graphs/commit-activity) 
[![Languages](https://img.shields.io/github/languages/count/jasonsarwar/jms-social-media.svg?color=orange)](https://github.com/JasonSarwar/jms-social-media) 
[![Repo Size](https://img.shields.io/github/repo-size/jasonsarwar/jms-social-media.svg?color=yellow)](https://github.com/JasonSarwar/jms-social-media)
-----
JMS Social Media is a personal simple social media website. The website is currently up on [http://bit.ly/jms-social-media].

## Technologies and Services Used in this Project
- Java
- AngularJS
- JavaScript
- Maven
- Gradle
- MySQL
- Redis
- Docker
- TravisCI
- Google Cloud Platform
- SonarQube

## Java Libraries Used in this Project
- SparkJava https://github.com/perwendel/spark
- Mybatis https://github.com/mybatis/mybatis-3
- MySQL ConnectorJ https://github.com/mysql/mysql-connector-j
- Gson https://github.com/google/gson
- Jackson FasterXML https://github.com/FasterXML/jackson
- Guava https://github.com/google/guava
- jjwt https://github.com/jwtk/jjwt
- Spring Security https://github.com/spring-projects/spring-security
- Jedis https://github.com/xetorthio/jedis
- DropWizard Metrics https://github.com/dropwizard/metrics
- Mockito https://github.com/mockito/mockito

## How to Run the Project

The application requires a properties file. The default properties file is located at [application-mock.properties](api/src/main/resources/application-mock.properties).
If the mock properties are used, the application will use its own heap as a data store.

The [application-dev2.properties](api/src/main/resources/application-dev2.properties) file allows the program to connect to a SQL Database to be used as a persistent data store.

The [application-dev-redis.properties](api/src/main/resources/application-dev-redis.properties) file allows the program to connect to both a SQL Database and a Redis instance.

To run with maven:
```make
make mavenrun
```
or 
```bash
mvn package exec:java -Dexec.mainClass="com.jms.socialmedia.app.App"
```

To run with Gradle:
```make
make gradlerun
```
or
```bash
gradle build run
```
The application will run on `localhost:4567/` or on port `4567` by default.

If you have Docker and Maven installed, you can run
```make
make dockerrun
```
to run a stand-alone Docker image with the Mock Data Service.

If you have Docker and Gradle installed, you can run
```make
make dockerrunwithgradle
```

If you have Docker Compose and Maven installed, you can run
```make
make dockercompose
```
to run the complete app with the MySQL and Redis Docker images.

If you have Docker Compose and Gradle installed, you can run
```make
make dockercomposewithgradle
```

If you are using the Mock DataService (not using a SQL Database), you can login to the website with these credentials:
Username: user
Password: password

Click on the "Log in" button to Post, Comment, and Like!
