# jms-social-media

[![Build Status](https://travis-ci.org/JasonSarwar/jms-social-media.svg?branch=master)](https://travis-ci.org/JasonSarwar/jms-social-media) [![<Sonarcloud Quality Gate>](https://sonarcloud.io/api/project_badges/measure?project=JasonSarwar_jms-social-media&metric=alert_status)](https://sonarcloud.io/dashboard?id=JasonSarwar_jms-social-media) [![codecov](https://codecov.io/gh/JasonSarwar/jms-social-media/branch/master/graph/badge.svg)](https://codecov.io/gh/JasonSarwar/jms-social-media) ![Website](https://img.shields.io/website/http/34.73.4.170.svg?down_message=offline&up_message=running) [![GitHub version](https://badge.fury.io/gh/JasonSarwar%2Fjms-social-media.svg)](https://badge.fury.io/gh/JasonSarwar%2Fjms-social-media) [![Commit Activity](https://img.shields.io/github/commit-activity/m/jasonsarwar/jms-social-media.svg)](https://github.com/JasonSarwar/jms-social-media/graphs/commit-activity) [![Languages](https://img.shields.io/github/languages/count/jasonsarwar/jms-social-media.svg?color=orange)](https://github.com/JasonSarwar/jms-social-media) [![Repo Size](https://img.shields.io/github/repo-size/jasonsarwar/jms-social-media.svg?color=yellow)](https://github.com/JasonSarwar/jms-social-media)
-----
The application requires a properties file. The default properties file is located at [application-mock.properties](api-sparkjava/src/main/resources/application-mock.properties).
If the mock properties are used, the application will use its heap as a data store.

The [application-dev.properties](api-sparkjava/src/main/resources/application-dev.properties) file allows the program to connect to a SQL Database to be used as a persistent data store. SQL scripts for creating the required tables will be posted soon.


To run with maven:
```make
make mavenrun
```
or run the maven_install script (maven_install.bat for Windows or maven_install.sh for Linux/Mac) and then run 
```bash
mvn exec:java -Dexec.mainClass="com.jms.socialmedia.app.App"
```

To run with Gradle:
```make
make gradlerun
```
or
```bash
gradle build run
```
The application will run on `localhost:4567/`

Mock Login Credentials:
Username: user
Password: password

Click on the "Log in" button to Post, Comment, and Like!

The website is currently up on [http://34.73.4.170].