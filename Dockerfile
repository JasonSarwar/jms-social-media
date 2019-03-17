# Gradle with JRE (Java) 11
FROM gradle:5.2.1-jre11-slim

# App Directory
WORKDIR /app

# Make the user 'gradle' the owner of the app directory
ADD --chown=gradle . /app

# All relevant files required for app
COPY ./core/src /app/core/src
COPY ./core/lib /app/core/lib
COPY ./core/build.gradle /app/core/build.gradle
COPY ./core/settings.gradle /app/core/settings.gradle
COPY ./api-sparkjava/src /app/api-sparkjava/src
COPY ./api-sparkjava/build.gradle /app/api-sparkjava/build.gradle
COPY ./api-sparkjava/settings.gradle /app/api-sparkjava/settings.gradle
COPY ./build.gradle /app/build.gradle
COPY ./settings.gradle /app/settings.gradle

# Make port 4567 available to the world outside this container
EXPOSE 4567

# Use Gradle to build and run the App
CMD ["gradle", "clean", "build", "run"]